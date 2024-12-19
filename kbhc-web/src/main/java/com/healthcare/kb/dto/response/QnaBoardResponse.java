package com.healthcare.kb.dto.response;

import com.healthcare.kb.dto.QnaBoardDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

}
