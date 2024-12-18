package com.healthcare.kb.service;

import com.healthcare.kb.domain.Role;
import com.healthcare.kb.domain.User;
import com.healthcare.kb.dto.UserDto;
import com.healthcare.kb.exception.NotFoundException;
import com.healthcare.kb.repository.RoleRepository;
import com.healthcare.kb.repository.UserRepository;
import com.healthcare.kb.type.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.healthcare.kb.constant.MessageConst.DATA_NOT_FOUND;
import static com.healthcare.kb.constant.MessageConst.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public void signUpUser(final UserDto.SignUp userDto){
        //초기 가입시 멤버로 가입. 권한 변경 API를 통해 변경.
        Role role = roleRepository.findByRoleType(RoleType.ROLE_MEMBER)
                .orElseThrow(() -> new NotFoundException(DATA_NOT_FOUND));

        userRepository.save(userDto.toEntity(role));
    }

    public boolean checkDuplicateEmail(String userId){
        return userRepository.findByEmail(userId).isPresent();
    }

    public UserDto.UserInfo findUserByUserNo(Long userNo){
        final User user = userRepository.findById(userNo)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        return UserDto.UserInfo.from(user);
    }

    public UserDto.UserInfo findUserByEmail(String userId){
        final User user = userRepository.findByEmail(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        return UserDto.UserInfo.from(user);
    }

}
