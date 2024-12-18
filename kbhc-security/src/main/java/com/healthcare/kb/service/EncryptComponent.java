package com.healthcare.kb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@RequiredArgsConstructor
public class EncryptComponent {

    private final BytesEncryptor encryptor;

    public String encryptData(String data) {
        return Base64.getEncoder().encodeToString(encryptor.encrypt(data.getBytes()));

    }

    public String decryptData(String encryptData) {
        return new String(encryptor.decrypt(Base64.getDecoder().decode(encryptData)));

    }

}