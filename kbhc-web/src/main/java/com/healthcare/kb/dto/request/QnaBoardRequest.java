package com.healthcare.kb.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.domain.PageRequest;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QnaBoardRequest {

    @Getter
    @Builder
    public static class Regist{
        private String title;
        private String contents;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PageablePostSearchRequest {

        @Schema(description = "페이지 커서", example = "0")
        @NotNull
        @Min(0)
        private final Integer offset;

        @Schema(description = "페이지 당 조회 개수", example = "10")
        @NotNull
        @Min(1)
        private final Integer limit;

        @Schema(description = "게시물 제목")
        private final String title;

        public PageRequest getPageRequest() {
            int correctionOffset = Math.max(this.getOffset() - 1, 0);   //페이지 보정
            return PageRequest.of(correctionOffset, this.getLimit());
        }
    }

}
