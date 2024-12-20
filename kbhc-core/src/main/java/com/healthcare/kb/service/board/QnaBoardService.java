package com.healthcare.kb.service.board;

import com.healthcare.kb.domain.board.QnaBoard;
import com.healthcare.kb.domain.User;
import com.healthcare.kb.domain.board.QnaComment;
import com.healthcare.kb.dto.BoardDto;
import com.healthcare.kb.dto.FileDto;
import com.healthcare.kb.dto.board.QnaBoardDto;
import com.healthcare.kb.exception.ForbiddenException;
import com.healthcare.kb.exception.NotFoundException;
import com.healthcare.kb.repository.UserRepository;
import com.healthcare.kb.repository.board.qna.QnaBoardRepository;
import com.healthcare.kb.repository.board.qna.QnaCommentRepository;
import com.healthcare.kb.service.BoardService;
import com.healthcare.kb.service.FileService;
import com.healthcare.kb.type.BoardType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.healthcare.kb.constant.MessageConst.*;

@Service
@RequiredArgsConstructor
public class QnaBoardService implements BoardService<QnaBoardDto.QnaPostDetail> {

    private final QnaBoardRepository qnaBoardRepository;
    private final QnaCommentRepository qnaCommentRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    /**
     * 서비스 구현체 확인용 메서드
     * @param boardType
     * @return
     */
    @Override
    public boolean isValidService(BoardType boardType) {
        return boardType.equals(BoardType.QNA);
    }

    /**
     * 게시글 저장
     * @param dto
     * @return
     */
    @Override
    @Transactional
    public Long savePostData(BoardDto.PostInfo dto) {
        QnaBoardDto.QnaPostInfo qnaDto = getQnaBoardDto(dto);
        User user = userRepository.findById(qnaDto.getCreatedBy())
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        return qnaBoardRepository.save(qnaDto.toEntity(user)).getQnaNo();
    }

    /**
     * 게시글 수정
     * @param postNo
     * @param dto
     */
    @Override
    @Transactional
    public void updatePostData(Long postNo, BoardDto.PostInfo dto) {
        QnaBoardDto.QnaPostInfo qnaDto = getQnaBoardDto(dto);
        User user = userRepository.findById(qnaDto.getCreatedBy())
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        QnaBoard qnaBoard = qnaBoardRepository.findById(postNo)
                .orElseThrow(() -> new NotFoundException(DATA_NOT_FOUND));

        //등록자가 아닐 경우 예외처리
        if(!qnaBoard.getCreatedBy().equals(user)){
            throw new ForbiddenException(FORBIDDEN_AUTHORIZED);
        }

        qnaBoard.updateQnaBoardData(qnaDto);
    }

    /**
     * 게시글 조회
     * @param postNo
     * @param userNo
     */
    @Override
    @Transactional
    public void deletePostData(Long postNo, Long userNo) {
        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        QnaBoard qnaBoard = qnaBoardRepository.findById(postNo)
                .orElseThrow(() -> new NotFoundException(DATA_NOT_FOUND));

        //등록자가 아닐 경우 예외처리
        if(!qnaBoard.getCreatedBy().equals(user)){
            throw new ForbiddenException(FORBIDDEN_AUTHORIZED);
        }

        //댓글 삭제
        List<QnaComment> comments = qnaCommentRepository.findAllByQnaBoard(qnaBoard)
                .orElse(Collections.emptyList());

        qnaBoardRepository.delete(qnaBoard);
        qnaCommentRepository.deleteAll(comments);
    }

    /**
     * 게시글 조회
     * @param qnaNo
     * @return
     */
    @Override
    public QnaBoardDto.QnaPostDetail findPostData(Long qnaNo) {
        QnaBoard qnaBoard = qnaBoardRepository.findById(qnaNo)
                .orElseThrow(() -> new NotFoundException(DATA_NOT_FOUND));

        List<FileDto.FileInfo> allFileList = Optional.of(fileService.findAllFileList(qnaNo, BoardType.QNA))
                .orElse(Collections.emptyList());
        return QnaBoardDto.QnaPostDetail.fromEntityToDetail(qnaBoard, allFileList);
    }

    /**
     * 게시글 리스트 조회
     * @param criteria
     * @param pageable
     * @return
     */
    @Override
    public Page<QnaBoardDto.QnaPostDetail> findPostDataList(BoardDto.SearchCreteria criteria, Pageable pageable){
        Page<QnaBoard> qnaSummaries = qnaBoardRepository.getPagination(criteria, pageable);

        final List<QnaBoardDto.QnaPostDetail> boardInfoList = qnaSummaries.getContent().stream()
                .map(QnaBoardDto.QnaPostDetail::fromEntityToSummary)
                .toList();

        return new PageImpl<>(boardInfoList, pageable, qnaSummaries.getTotalElements());
    }

    /**
     * 답글 저장
     * @param dto
     * @return
     */
    @Override
    @Transactional
    public Long savePostCommentData(BoardDto.CommentRegist dto) {
        QnaBoardDto.QnaCommentRegist commentDto = getQnaCommentDto(dto);
        User user = userRepository.findById(commentDto.getCreatedBy())
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        QnaBoard board= qnaBoardRepository.findById(commentDto.getPostNo())
                .orElseThrow(() -> new NotFoundException(DATA_NOT_FOUND));

        return qnaCommentRepository.save(commentDto.toEntity(user, board)).getCommentNo();
    }

    /**
     * 답글 수정
     * @param commentNo
     * @param dto
     */
    @Override
    @Transactional
    public void updatePostCommentData(Long commentNo, BoardDto.CommentRegist dto) {
        QnaBoardDto.QnaCommentRegist commentDto = getQnaCommentDto(dto);
        User user = userRepository.findById(commentDto.getCreatedBy())
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        QnaComment qnaComment = qnaCommentRepository.findById(commentNo)
                .orElseThrow(() -> new NotFoundException(DATA_NOT_FOUND));

        //등록자가 아닐 경우 예외처리
        if(!qnaComment.getCreatedBy().equals(user)){
            throw new ForbiddenException(FORBIDDEN_AUTHORIZED);
        }

        qnaComment.updateQnaBoardData(commentDto);
    }

    /**
     * 답글 삭제
     * @param commentNo
     * @param userNo
     */
    @Override
    @Transactional
    public void deletePostCommentData(Long commentNo, Long userNo) {
        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        QnaComment qnaComment = qnaCommentRepository.findById(commentNo)
                .orElseThrow(() -> new NotFoundException(DATA_NOT_FOUND));

        //등록자가 아닐 경우 예외처리
        if(!qnaComment.getCreatedBy().equals(user)){
            throw new ForbiddenException(FORBIDDEN_AUTHORIZED);
        }

        qnaCommentRepository.delete(qnaComment);
    }

    protected QnaBoardDto.QnaPostInfo getQnaBoardDto(BoardDto.PostInfo dto) {
        return Optional.of(dto)
                .filter(QnaBoardDto.QnaPostInfo.class::isInstance)
                .map(QnaBoardDto.QnaPostInfo.class::cast)
                .orElseThrow(() -> new NotFoundException(DATA_NOT_FOUND));
    }

    protected QnaBoardDto.QnaCommentRegist getQnaCommentDto(BoardDto.CommentRegist dto) {
        return Optional.of(dto)
                .filter(QnaBoardDto.QnaCommentRegist.class::isInstance)
                .map(QnaBoardDto.QnaCommentRegist.class::cast)
                .orElseThrow(() -> new NotFoundException(DATA_NOT_FOUND));
    }

}
