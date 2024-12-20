package com.healthcare.kb.service.board;

import com.healthcare.kb.domain.QnaBoard;
import com.healthcare.kb.domain.User;
import com.healthcare.kb.dto.QnaBoardDto;
import com.healthcare.kb.exception.ForbiddenException;
import com.healthcare.kb.exception.NotFoundException;
import com.healthcare.kb.repository.board.qna.QnaBoardRepository;
import com.healthcare.kb.repository.UserRepository;
import com.healthcare.kb.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.healthcare.kb.constant.MessageConst.*;

@Service
@RequiredArgsConstructor
public class QnaBoardService implements BoardService {

    private final QnaBoardRepository qnaBoardRepository;
    private final UserRepository userRepository;

    @Transactional
    public void saveQnaPost(QnaBoardDto.QnaRegist dto){
        User user = userRepository.findById(dto.getCreatedBy())
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        qnaBoardRepository.save(dto.toEntity(user));
    }

    @Transactional
    public void updateQnaPost(Long qnaNo, QnaBoardDto.QnaRegist dto){
        User user = userRepository.findById(dto.getCreatedBy())
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        QnaBoard qnaBoard = qnaBoardRepository.findById(qnaNo)
                .orElseThrow(() -> new NotFoundException(DATA_NOT_FOUND));

        //등록자가 아닐 경우 예외처리
        if(!qnaBoard.getCreatedBy().equals(user)){
            throw new ForbiddenException(FORBIDDEN_AUTHORIZED);
        }

        qnaBoard.updateQnaBoardData(dto);
    }

    @Transactional
    public void deleteQnaPost(Long qnaNo, Long userNo){
        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        QnaBoard qnaBoard = qnaBoardRepository.findById(qnaNo)
                .orElseThrow(() -> new NotFoundException(DATA_NOT_FOUND));

        //등록자가 아닐 경우 예외처리
        if(!qnaBoard.getCreatedBy().equals(user)){
            throw new ForbiddenException(FORBIDDEN_AUTHORIZED);
        }

        qnaBoardRepository.delete(qnaBoard);
    }


    public QnaBoardDto.QnaDetail findQnaPost(Long qnaNo){
        QnaBoard qnaBoard = qnaBoardRepository.findById(qnaNo)
                .orElseThrow(() -> new NotFoundException(DATA_NOT_FOUND));

        return QnaBoardDto.QnaDetail.from(qnaBoard);
    }

    public Page<QnaBoardDto.QnaSummary> selectPostList(QnaBoardDto.SearchCreteria criteria, Pageable pageable){
        Page<QnaBoard> qnaSummaries = qnaBoardRepository.getPagination(criteria, pageable);

        final List<QnaBoardDto.QnaSummary> boardInfoList = qnaSummaries.getContent().stream()
                .map(QnaBoardDto.QnaSummary::from)
                .toList();

        return new PageImpl<>(boardInfoList, pageable, qnaSummaries.getTotalElements());
    }



}
