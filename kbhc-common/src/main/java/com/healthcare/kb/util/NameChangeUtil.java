package com.healthcare.kb.util;

import org.springframework.util.StringUtils;

public class NameChangeUtil {

    public static String convertUserName(String nickName){
        if (!StringUtils.hasText(nickName)) {
            return "";
        }

        // 첫 글자만 남기고 나머지 마스킹
        String firstChar = nickName.substring(0, 1);
        String maskedPart = "*".repeat(nickName.length() - 1);

        return firstChar + maskedPart;
    }

}
