package com.healthcare.kb.domain.board;

import com.healthcare.kb.domain.BaseEntity;
import com.healthcare.kb.domain.File;
import com.healthcare.kb.domain.User;
import com.healthcare.kb.dto.BoardDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Table(name = "t_qna_board")
@SQLDelete(sql = "UPDATE t_qna_board SET deleted = true where qna_no = ?")
@SQLRestriction("deleted = false")  //삭제가 아닌 유저만 조회하도록 조건처리
@NoArgsConstructor(access = PROTECTED)
public class QnaBoard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qnaNo;

    @Column
    private String title;

    @Column
    private String contents;

    @Column
    private Long viewCnt;

    @OneToMany(mappedBy = "qnaBoard", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<QnaComment> commentList;

    @ManyToOne
    @JoinColumn(name = "user_no")
    private User createdBy;

    public void updateQnaBoardData(BoardDto.PostInfo dto){
        this.title = dto.getTitle();
        this.contents = dto.getContents();
    }

    public void updateViewCnt(){
        this.viewCnt++;
    }

    @Builder
    public QnaBoard(String title, String contents, Long viewCnt, User createdBy) {
        this.title = title;
        this.contents = contents;
        this.viewCnt = viewCnt;
        this.createdBy = createdBy;
    }
}
