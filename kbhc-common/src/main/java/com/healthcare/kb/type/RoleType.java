package com.healthcare.kb.type;

import com.healthcare.kb.exception.DataProcessException;
import jakarta.persistence.AttributeConverter;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

import static com.healthcare.kb.constant.MessageConst.*;

@Getter
public enum RoleType {
    ROLE_EXPERT, ROLE_MEMBER;

    public static class Converter implements AttributeConverter<RoleType, String> {

        @Override
        public String convertToDatabaseColumn(final RoleType attribute) {
            if (attribute == null) {
                return null;
            }
            return attribute.name();
        }

        @Override
        public RoleType convertToEntityAttribute(final String dbData) {
            if (dbData == null) {
                return null;
            }
            return Arrays.stream(RoleType.values())
                    .filter(idType -> Objects.equals(idType.name(), dbData))
                    .findAny()
                    .orElseThrow(() -> new DataProcessException(SERVER_PROCESS_ERROR));
        }
    }

}
