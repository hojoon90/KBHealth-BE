package com.healthcare.kb.type;

import com.healthcare.kb.exception.NotFoundException;
import jakarta.persistence.AttributeConverter;

import java.util.Arrays;
import java.util.Objects;

import static com.healthcare.kb.constant.MessageConst.DATA_NOT_FOUND;

public enum BoardType {
    QNA
    ;

    public static class Converter implements AttributeConverter<BoardType, String> {

        @Override
        public String convertToDatabaseColumn(final BoardType attribute) {
            if (attribute == null) {
                return null;
            }
            return attribute.name();
        }

        @Override
        public BoardType convertToEntityAttribute(final String dbData) {
            if (dbData == null) {
                return null;
            }
            return Arrays.stream(BoardType.values())
                    .filter(csReasonCode -> Objects.equals(csReasonCode.name(), dbData))
                    .findAny()
                    .orElseThrow(() -> new NotFoundException(DATA_NOT_FOUND));
        }
    }
}
