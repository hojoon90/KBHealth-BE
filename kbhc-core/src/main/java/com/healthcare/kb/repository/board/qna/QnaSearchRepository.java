package com.healthcare.kb.repository.board.qna;

import com.healthcare.kb.domain.board.QnaBoard;
import com.healthcare.kb.dto.BoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QnaSearchRepository {

    Page<QnaBoard> getPagination(BoardDto.SearchCreteria criteria, Pageable pageable);
}
