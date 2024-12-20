package com.healthcare.kb.repository.board.qna;

import com.healthcare.kb.domain.QnaBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QnaBoardRepository extends JpaRepository<QnaBoard, Long>, QnaSearchRepository {
}
