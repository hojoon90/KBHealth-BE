package com.healthcare.kb.domain.board;

import com.healthcare.kb.domain.BaseEntity;
import com.healthcare.kb.domain.User;
import com.healthcare.kb.dto.BoardDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Table(name = "t_qna_comment")
@SQLDelete(sql = "UPDATE t_qna_comment SET deleted = true where comment_no = ?")
@SQLRestriction("deleted = false")  //삭제가 아닌 유저만 조회하도록 조건처리
@NoArgsConstructor(access = PROTECTED)
public class QnaComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentNo;

    @ManyToOne
    @JoinColumn(name = "qna_no")
    private QnaBoard qnaBoard;

    @Column
    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_no")
    private User createdBy;

    public void updateQnaBoardData(BoardDto.CommentRegist dto){
        this.contents = dto.getContents();
    }

    @Builder
    public QnaComment(String contents, QnaBoard qnaBoard, User createdBy) {
        this.contents = contents;
        this.qnaBoard = qnaBoard;
        this.createdBy = createdBy;
    }

}
