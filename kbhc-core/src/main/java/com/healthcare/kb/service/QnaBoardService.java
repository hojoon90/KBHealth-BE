package com.healthcare.kb.service;

import com.healthcare.kb.domain.User;
import com.healthcare.kb.dto.QnaBoardDto;
import com.healthcare.kb.exception.NotFoundException;
import com.healthcare.kb.repository.QnaBoardRepository;
import com.healthcare.kb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.healthcare.kb.constant.MessageConst.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class QnaBoardService {

    private final QnaBoardRepository qnaBoardRepository;
    private final UserRepository userRepository;

    @Transactional
    public void saveQnaPost(QnaBoardDto.QnaRegist dto){
        User user = userRepository.findById(dto.getCreatedBy())
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        qnaBoardRepository.save(dto.toEntity(user));
    }


}
