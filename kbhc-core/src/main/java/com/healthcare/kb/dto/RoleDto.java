package com.healthcare.kb.dto;

import com.healthcare.kb.domain.Role;
import com.healthcare.kb.type.RoleType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RoleDto {

    @Getter
    @Builder
    public static class RoleInfo{
        RoleType roleType;

        public static RoleInfo from(Role role){
            return RoleInfo.builder()
                    .roleType(role.getRoleType())
                    .build();
        }
    }
}
