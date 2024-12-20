package com.healthcare.kb.repository.board.qna;

import com.healthcare.kb.domain.QnaBoard;
import com.healthcare.kb.dto.QnaBoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QnaSearchRepository {

    Page<QnaBoard> getPagination(QnaBoardDto.SearchCreteria criteria, Pageable pageable);
}
