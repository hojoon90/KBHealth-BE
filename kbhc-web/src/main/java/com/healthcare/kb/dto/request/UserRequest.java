package com.healthcare.kb.dto.request;

import com.healthcare.kb.type.RoleActionType;
import com.healthcare.kb.type.RoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRequest {

    @Getter
    @Builder
    public static class Signup {

        @Schema(description = "회원 아이디", example = "test@test.com")
        @NotBlank
        @Email()
        private String email;

        @Schema(description = "비밀번호", example = "1234")
        @NotBlank
        private String password;

        @Schema(description = "이름", example = "테스트")
        @NotBlank
        private String name;

        @Schema(description = "닉네임", example = "테스트 닉네임")
        @NotBlank
        private String nickName;
    }

    @Getter
    @Builder
    public static class Login{

        @Schema(description = "회원 아이디", example = "test@test.com")
        @NotBlank
        @Email
        private String email;

        @Schema(description = "비밀번호", example = "1234")
        @NotBlank
        private String password;
    }

    @Getter
    @Builder
    public static class Refresh{

        @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiJ9.refresh.token")
        @NotBlank
        private String refreshToken;
    }

    @Getter
    @Builder
    public static class UserRole{

        @NotBlank
        @Email
        private String email;
        @NotBlank
        private String changeRole;
        @NotBlank
        private String roleAction;
    }

}
