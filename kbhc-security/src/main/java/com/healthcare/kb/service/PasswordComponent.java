package com.healthcare.kb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordComponent {

    private final PasswordEncoder passwordEncoder;

    public final String encodePassword(String password){
        return passwordEncoder.encode(password);
    }

    public final boolean checkUserValidation(String rawPassword, String password){
        return passwordEncoder.matches(rawPassword, password);
    }


}
