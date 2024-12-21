package com.healthcare.kb.facade;

import com.healthcare.kb.dto.AppResponse;
import com.healthcare.kb.dto.JwtDto;
import com.healthcare.kb.dto.LoginUser;
import com.healthcare.kb.dto.UserDto;
import com.healthcare.kb.dto.request.UserRequest;
import com.healthcare.kb.dto.response.UserResponse;
import com.healthcare.kb.exception.AuthorizeException;
import com.healthcare.kb.exception.ConflictException;
import com.healthcare.kb.exception.NotFoundException;
import com.healthcare.kb.service.EncryptComponent;
import com.healthcare.kb.service.KbhcSecurityService;
import com.healthcare.kb.service.PasswordComponent;
import com.healthcare.kb.service.UserService;
import com.healthcare.kb.type.RoleActionType;
import com.healthcare.kb.type.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.healthcare.kb.constant.MessageConst.*;

@Service
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;
    private final PasswordComponent passwordComponent;
    private final EncryptComponent encryptComponent;
    private final KbhcSecurityService kbhcSecurityService;

    /**
     * 회원 가입
     * @param request
     * @return
     */
    public AppResponse<Void> signupUser(UserRequest.Signup request){

        if(userService.checkDuplicateEmail(request.getEmail())){
            throw new ConflictException(USER_DATA_CONFLICT);
        }

        String encodePassword = passwordComponent.encodePassword(request.getPassword());

        UserDto.SignUp dto = UserDto.SignUp.builder()
                .email(request.getEmail())
                .password(encodePassword)
                .name(encryptComponent.encryptData(request.getName()))
                .nickName(request.getNickName())
                .build();

        userService.signUpUser(dto);
        return AppResponse.responseVoidSuccess(HttpStatus.CREATED.value());
    }

    /**
     * 로그인 처리
     * @param request
     * @return
     */
    public AppResponse<UserResponse.TokenResponse> login(UserRequest.Login request) {

        UserDto.UserInfo userDto = userService.findUserByEmail(request.getEmail());

        //비밀번호 체크
        boolean isValid = passwordComponent.checkUserValidation(request.getPassword(), userDto.getPassword());
        if (!isValid) throw new AuthorizeException(WRONG_PASSWORD);

        LoginUser.Info userInfo = LoginUser.Info.builder()
                .userNo(userDto.getUserNo())
                .email(userDto.getEmail())
                .name(userDto.getName())
                .nickName(userDto.getNickName())
                .role(userDto.getRoles())
                .build();

        final JwtDto.Tokens tokens = kbhcSecurityService.getTokens(userInfo);
        return AppResponse.responseSuccess(UserResponse.TokenResponse.from(tokens));
    }

    /**
     * 리프레시토큰 갱신처리
     * @param request
     * @return
     */
    public AppResponse<UserResponse.TokenResponse> getRefreshToken(UserRequest.Refresh request){

        Long userNo = kbhcSecurityService.extractUserNo(request.getRefreshToken())
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        UserDto.UserInfo userDto = userService.findUserByUserNo(userNo);

        LoginUser.Info userInfo = LoginUser.Info.builder()
                .userNo(userDto.getUserNo())
                .email(userDto.getEmail())
                .name(userDto.getName())
                .nickName(userDto.getNickName())
                .role(userDto.getRoles())
                .build();

        final JwtDto.Tokens tokens = kbhcSecurityService.getTokens(userInfo);
        return AppResponse.responseSuccess(UserResponse.TokenResponse.from(tokens));
    }

    public AppResponse<Void> changeUserRole(UserRequest.UserRole request){

        RoleType roleType = RoleType.valueOf(request.getChangeRole());
        RoleActionType roleActionType = RoleActionType.valueOf(request.getRoleAction());

        UserDto.UserRole dto = UserDto.UserRole.builder()
                .email(request.getEmail())
                .changeRole(roleType)
                .roleAction(roleActionType)
                .build();

        userService.updateUserRole(dto);
        return AppResponse.responseVoidSuccess(HttpStatus.OK.value());
    }

}
