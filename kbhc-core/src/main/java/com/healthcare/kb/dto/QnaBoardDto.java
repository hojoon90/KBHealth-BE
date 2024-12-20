package com.healthcare.kb.dto;

import com.healthcare.kb.domain.QnaBoard;
import com.healthcare.kb.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QnaBoardDto {

    @Getter
    @Builder
    public static class QnaRegist{
        private String title;
        private String contents;
        private Long viewCnt;
        private Long createdBy;

        public QnaBoard toEntity(User user){
            return QnaBoard.builder()
                    .title(title)
                    .contents(contents)
                    .viewCnt(0L)
                    .createdBy(user)
                    .build();
        }

    }

    @Getter
    @Builder
    public static class QnaSummary{
        private Long qnaNo;
        private String title;
        private Long viewCnt;
        private String createdBy;
        private LocalDateTime createdAt;

        public static QnaSummary from(QnaBoard entity) {
            return QnaSummary.builder()
                    .qnaNo(entity.getQnaNo())
                    .title(entity.getTitle())
                    .viewCnt(entity.getViewCnt())
                    .createdBy(entity.getCreatedBy().getNickName())
                    .createdAt(entity.getCreatedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class QnaDetail{
        private Long qnaNo;
        private String title;
        private String contents;
        private Long viewCnt;
        private String createdBy;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static QnaDetail from(QnaBoard entity){
            return QnaDetail.builder()
                    .qnaNo(entity.getQnaNo())
                    .title(entity.getTitle())
                    .contents(entity.getContents())
                    .viewCnt(entity.getViewCnt())
                    .createdAt(entity.getCreatedAt())
                    .createdBy(entity.getCreatedBy().getNickName())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class SearchCreteria{
        private String title;
    }
}
