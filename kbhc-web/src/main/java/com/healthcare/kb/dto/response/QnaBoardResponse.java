package com.healthcare.kb.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.healthcare.kb.dto.BoardDto;
import com.healthcare.kb.dto.board.QnaBoardDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QnaBoardResponse {

    @Getter
    @Builder
    public static class QnaDetail {
        private Long qnaNo;
        private String title;
        private String contents;
        private Long viewCnt;
        private String createdBy;
        private List<CommentDetail> commentList;
        private List<FileResponse.FileInfo> fileList;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime updatedAt;

        public static QnaDetail from(BoardDto.PostDetail dto){
            List<CommentDetail> commentList = dto.getCommentList()
                    .stream().map(CommentDetail::from).toList();

            List<FileResponse.FileInfo> fileList = dto.getFileList()
                    .stream().map(FileResponse.FileInfo::from).toList();
            
            return QnaDetail.builder()
                    .qnaNo(dto.getPostNo())
                    .title(dto.getTitle())
                    .contents(dto.getContents())
                    .viewCnt(dto.getViewCnt())
                    .commentList(commentList)
                    .fileList(fileList)
                    .createdBy(dto.getCreatedBy())
                    .createdAt(dto.getCreatedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class CommentDetail {
        private Long commentNo;
        private String contents;
        private String createdBy;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime updatedAt;

        public static CommentDetail from(BoardDto.CommentDetail dto){
            return CommentDetail.builder()
                    .commentNo(dto.getCommentNo())
                    .contents(dto.getContents())
                    .createdBy(dto.getCreatedBy())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class QnaSummary {
        private Long qnaNo;
        private String title;
        private Long viewCnt;
        private String createdBy;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;

        public static QnaSummary from(QnaBoardDto.QnaPostDetail dto){
            return QnaSummary.builder()
                    .qnaNo(dto.getPostNo())
                    .title(dto.getTitle())
                    .viewCnt(dto.getViewCnt())
                    .createdBy(dto.getCreatedBy())
                    .createdAt(dto.getCreatedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class QnaBoardPages{
        private long count;
        private int totalPages;
        private long pageSize;
        private List<QnaSummary> qnaSummaries;

        public static QnaBoardPages of(long count, int totalPages, long pageSize, List<QnaBoardDto.QnaPostDetail> postDetails) {
            final List<QnaSummary> collect = postDetails.stream().map(QnaSummary::from).toList();
            return QnaBoardPages.builder()
                    .count(count)
                    .totalPages(totalPages)
                    .pageSize(pageSize)
                    .qnaSummaries(collect)
                    .build();
        }
    }

}
