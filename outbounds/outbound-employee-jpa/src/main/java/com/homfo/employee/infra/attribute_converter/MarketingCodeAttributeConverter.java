package com.homfo.employee.infra.attribute_converter;

import com.homfo.enums.MarketingCode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

/**
 * MarketingCode enum을 DB 값과 매칭시킵니다.
 */
@Converter
@Slf4j
public class MarketingCodeAttributeConverter implements AttributeConverter<MarketingCode, String> {
    @Override
    public String convertToDatabaseColumn(MarketingCode attribute) {
        if (attribute == null) return null;
        return attribute.getCode();
    }

    @Override
    public MarketingCode convertToEntityAttribute(String dbData) {
        return MarketingCode.fromCode(dbData);
    }
}
