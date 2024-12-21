package com.healthcare.kb.dto.board;

import com.healthcare.kb.domain.board.QnaBoard;
import com.healthcare.kb.domain.User;
import com.healthcare.kb.domain.board.QnaComment;
import com.healthcare.kb.dto.BoardDto;
import com.healthcare.kb.dto.FileDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

import static com.healthcare.kb.dto.BoardDto.convertToCommentInfoList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QnaBoardDto {


    @Getter
    @SuperBuilder
    public static class QnaPostInfo extends BoardDto.PostInfo {

        public QnaBoard toEntity(User user){
            return QnaBoard.builder()
                    .title(super.getTitle())
                    .contents(super.getContents())
                    .viewCnt(0L)
                    .createdBy(user)
                    .build();
        }
    }

    @Getter
    @SuperBuilder
    public static class QnaPostDetail extends BoardDto.PostDetail {

        //https://community.sonarsource.com/t/potential-false-positive-for-s3252-with-lombok-superbuilder/38082/3
        //Builder 사용 관련 경고 제거
        @SuppressWarnings("java:S3252")
        public static QnaBoardDto.QnaPostDetail fromEntityToDetail (QnaBoard entity){

            List<QnaCommentDetail> commentList = entity.getCommentList()
                    .stream()
                    .map(QnaCommentDetail::from)
                    .toList();

            return QnaBoardDto.QnaPostDetail.builder()
                    .postNo(entity.getQnaNo())
                    .title(entity.getTitle())
                    .contents(entity.getContents())
                    .viewCnt(entity.getViewCnt())
                    .commentList(convertToCommentInfoList(commentList))
                    .createdAt(entity.getCreatedAt())
                    .createdBy(entity.getCreatedBy().getNickName())
                    .build();
        }

        @SuppressWarnings("java:S3252")
        public static QnaBoardDto.QnaPostDetail fromEntityToSummary (QnaBoard entity) {
            return QnaPostDetail.builder()
                    .postNo(entity.getQnaNo())
                    .title(entity.getTitle())
                    .viewCnt(entity.getViewCnt())
                    .createdBy(entity.getCreatedBy().getNickName())
                    .createdAt(entity.getCreatedAt())
                    .build();
        }
    }

    @Getter
    @SuperBuilder
    public static class QnaCommentRegist extends BoardDto.CommentRegist {

        public QnaComment toEntity(User user, QnaBoard board){
            return QnaComment.builder()
                    .contents(super.getContents())
                    .qnaBoard(board)
                    .createdBy(user)
                    .build();
        }

    }

    @Getter
    @SuperBuilder
    public static class QnaCommentDetail extends BoardDto.CommentDetail {

        public static QnaCommentDetail from(QnaComment entity){
            return QnaCommentDetail.builder()
                    .commentNo(entity.getCommentNo())
                    .contents(entity.getContents())
                    .createdBy(entity.getCreatedBy().getNickName())
                    .build();
        }
    }

}
