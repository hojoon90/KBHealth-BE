package com.healthcare.kb.service;

import com.healthcare.kb.domain.Role;
import com.healthcare.kb.domain.User;
import com.healthcare.kb.dto.UserDto;
import com.healthcare.kb.exception.NotFoundException;
import com.healthcare.kb.repository.RoleRepository;
import com.healthcare.kb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.healthcare.kb.constant.MessageConst.DATA_NOT_FOUND;
import static com.healthcare.kb.constant.MessageConst.USER_NOT_FOUND;
import static com.healthcare.kb.type.RoleType.MEMBER;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    /**
     * 회원 가입
     * @param userDto
     */
    @Transactional
    public void signUpUser(final UserDto.SignUp userDto){
        //초기 가입시 멤버로 가입. 권한 변경 API를 통해 변경.
        Role role = roleRepository.findByRoleType(MEMBER)
                .orElseThrow(() -> new NotFoundException(DATA_NOT_FOUND));

        userRepository.save(userDto.toEntity(role));
    }

    /**
     * 중복 회원 조회
     * @param userId
     * @return
     */
    public boolean checkDuplicateEmail(String userId){
        return userRepository.findByEmail(userId).isPresent();
    }

    /**
     * 회원 번호를 통한 조회
     * @param userNo
     * @return
     */
    public UserDto.UserInfo findUserByUserNo(Long userNo){
        final User user = userRepository.findById(userNo)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        return UserDto.UserInfo.from(user);
    }

    /**
     * 회원 아이디를 통한 조회
     * @param userId
     * @return
     */
    public UserDto.UserInfo findUserByEmail(String userId){
        final User user = userRepository.findByEmail(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        return UserDto.UserInfo.from(user);
    }

    @Transactional
    public void updateUserRole(UserDto.UserRole dto){

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        Role role = roleRepository.findByRoleType(dto.getChangeRole())
                .orElseThrow(() -> new NotFoundException(DATA_NOT_FOUND));

        user.updateRole(dto, role);

        // 권한이 하나도 없으면 기본 MEMBER 권한으로 적용.
        if(user.getRoles().isEmpty()) {
            Role defaultRole = roleRepository.findByRoleType(MEMBER)
                    .orElseThrow(() -> new NotFoundException(DATA_NOT_FOUND));
            user.updateDefaultRole(defaultRole);
        }
    }

}
