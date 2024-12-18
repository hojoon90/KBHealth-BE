package com.healthcare.kb.dto.response;

import com.healthcare.kb.dto.JwtDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponse {

    @Getter
    @Builder
    public static class TokenResponse {
        private String accessToken;
        private String refreshToken;

        public static TokenResponse from(JwtDto.Tokens dto){
            return TokenResponse.builder()
                    .accessToken(dto.getAccessToken())
                    .refreshToken(dto.getRefreshToken())
                    .build();
        }
    }

}
