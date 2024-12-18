package com.healthcare.kb.dto;

import com.healthcare.kb.type.RoleType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginUser {

    @Getter
    @Builder
    public static class Info {
        private Long userNo;
        private String email;
        private String name;
        private String nickName;
        private List<RoleType> role;
    }

}
