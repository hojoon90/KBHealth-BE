package com.healthcare.kb.dto;

import com.healthcare.kb.domain.Role;
import com.healthcare.kb.domain.User;
import com.healthcare.kb.type.RoleActionType;
import com.healthcare.kb.type.RoleType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDto {

    @Getter
    @Builder
    public static class SignUp {
        private String email;
        private String password;
        private String name;
        private String nickName;

        public User toEntity(Role role) {
            return User.builder()
                    .email(email)
                    .password(password)
                    .name(name)
                    .nickName(nickName)
                    .roles(Set.of(role))
                    .build();
        }
    }

    @Getter
    @Builder
    public static class UserInfo {
        private Long userNo;
        private String email;
        private String password;
        private String name;
        private String nickName;
        private List<RoleType> roles;

        public static UserInfo from(User user) {
            final List<RoleType> roles = user.getRoles()
                    .stream()
                    .map(Role::getRoleType)
                    .toList();

            return UserInfo.builder()
                    .userNo(user.getUserNo())
                    .email(user.getEmail())
                    .name(user.getName())
                    .password(user.getPassword())
                    .nickName(user.getNickName())
                    .roles(roles)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class UserRole{
        private String email;
        private RoleType changeRole;
        private RoleActionType roleAction;
    }
}
