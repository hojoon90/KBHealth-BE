package com.healthcare.kb.repository.board.qna;

import com.healthcare.kb.domain.board.QnaBoard;
import com.healthcare.kb.domain.board.QnaComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QnaCommentRepository extends JpaRepository<QnaComment, Long> {

    Optional<List<QnaComment>> findAllByQnaBoard(QnaBoard board);

}
