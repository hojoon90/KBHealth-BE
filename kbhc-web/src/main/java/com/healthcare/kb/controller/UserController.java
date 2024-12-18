package com.healthcare.kb.controller;

import com.healthcare.kb.dto.AppResponse;
import com.healthcare.kb.dto.request.UserRequest;
import com.healthcare.kb.dto.response.UserResponse;
import com.healthcare.kb.facade.UserFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kbhc")
public class UserController {

    private final UserFacade userFacade;

    @PostMapping("/signup")
    public ResponseEntity<AppResponse<Void>> signUpUser(
            @RequestBody @Valid final UserRequest.Signup request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userFacade.signupUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AppResponse<UserResponse.TokenResponse>> login(
            @RequestBody @Valid final UserRequest.Login request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userFacade.login(request));
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<AppResponse<UserResponse.TokenResponse>> refreshToken(
            @RequestBody @Valid final UserRequest.Refresh request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userFacade.getRefreshToken(request));
    }

}
