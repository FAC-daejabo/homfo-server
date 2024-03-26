package com.homfo.sms.infra.attribute_converter;


import com.homfo.sms.infra.enums.SmsCodeStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

/**
 * SmsCodeStatus enum을 DB 값과 매칭시킵니다.
 */
@Converter(autoApply = true)
@Slf4j
public class SmsCodeStatusAttributeConverter implements AttributeConverter<SmsCodeStatus, String> {

    @Override
    public String convertToDatabaseColumn(SmsCodeStatus attribute) {
        return attribute.getCode();
    }

    @Override
    public SmsCodeStatus convertToEntityAttribute(String dbData) {
        return SmsCodeStatus.fromCode(dbData);
    }
}
