package com.healthcare.kb.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtDto {

    @Getter
    @Builder
    public static class Tokens{

        private String accessToken;
        private String refreshToken;

    }

}
