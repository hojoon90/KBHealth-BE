package com.healthcare.kb.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QnaBoardRequest {

    @Getter
    @Builder
    public static class Regist{
        private String title;
        private String contents;
    }



}
