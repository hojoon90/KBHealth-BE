package com.healthcare.kb.dto.response;

import com.healthcare.kb.dto.QnaBoardDto;
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
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static QnaDetail from(QnaBoardDto.QnaDetail dto){
            return QnaDetail.builder()
                    .qnaNo(dto.getQnaNo())
                    .title(dto.getTitle())
                    .contents(dto.getContents())
                    .viewCnt(dto.getViewCnt())
                    .createdBy(dto.getCreatedBy())
                    .createdAt(dto.getCreatedAt())
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
        private LocalDateTime createdAt;

        public static QnaSummary from(QnaBoardDto.QnaSummary dto){
            return QnaSummary.builder()
                    .qnaNo(dto.getQnaNo())
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

        public static QnaBoardPages of(long count, int totalPages, long pageSize, List<QnaBoardDto.QnaSummary> postDetails) {
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
