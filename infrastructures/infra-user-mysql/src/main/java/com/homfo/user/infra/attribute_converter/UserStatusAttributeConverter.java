package com.homfo.user.infra.attribute_converter;


import com.homfo.user.infra.enums.UserStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

/**
 * UserStatus enum을 DB 값과 매칭시킵니다.
 */
@Converter
@Slf4j
public class UserStatusAttributeConverter implements AttributeConverter<UserStatus, String> {

    @Override
    public String convertToDatabaseColumn(UserStatus attribute) {
        return attribute.getCode();
    }

    @Override
    public UserStatus convertToEntityAttribute(String dbData) {
        return UserStatus.fromCode(dbData);
    }
}
