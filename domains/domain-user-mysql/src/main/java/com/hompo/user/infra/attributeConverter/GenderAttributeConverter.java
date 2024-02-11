package com.hompo.user.infra.attributeConverter;


import com.hompo.enums.Gender;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

/**
 * Gender enum을 DB 값과 매칭시킵니다.
 * */
@Converter
@Slf4j
public class GenderAttributeConverter implements AttributeConverter<Gender, String> {

    @Override
    public String convertToDatabaseColumn(Gender attribute) {
        if(attribute == null) return null;
        return attribute.getCode();
    }

    @Override
    public Gender convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            return Gender.fromCode(dbData);
        } catch (IllegalArgumentException e) {
            log.error("failure to convert cause unexpected code [{}]", dbData, e);
            throw e;
        }
    }
}
