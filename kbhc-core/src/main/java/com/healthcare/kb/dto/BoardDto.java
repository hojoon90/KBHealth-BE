package com.healthcare.kb.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardDto {

    @Getter
    @SuperBuilder
    public static class PostInfo {
        private String title;
        private String contents;
        private Long viewCnt;
        private Long createdBy;

    }

    @Getter
    @SuperBuilder
    public static class PostDetail {
        private Long postNo;
        private String title;
        private String contents;
        private Long viewCnt;
        private String createdBy;
        private List<CommentDetail> commentList;
        private List<FileDto.FileInfo> fileList;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

    }

    @Getter
    @SuperBuilder
    public static class CommentRegist {
        private Long postNo;
        private String contents;
        private Long createdBy;

    }

    @Getter
    @SuperBuilder
    public static class CommentDetail {
        private Long commentNo;
        private String contents;
        private String createdBy;

    }

    @Getter
    @Builder
    public static class SearchCreteria{
        private String title;
    }

    public static <T extends CommentDetail> List<CommentDetail> convertToCommentInfoList(
            List<T> commentList) {
        return commentList.stream()
                .map(comment -> (CommentDetail) comment)
                .toList();
    }
}
