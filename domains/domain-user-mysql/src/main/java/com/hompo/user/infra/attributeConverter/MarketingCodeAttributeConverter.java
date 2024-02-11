package com.hompo.user.infra.attributeConverter;

import com.hompo.enums.MarketingCode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

/**
 * MarketingCode enum을 DB 값과 매칭시킵니다.
 * */
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
        if (dbData == null) return null;
        try {
            return MarketingCode.fromCode(dbData);
        } catch (IllegalArgumentException e) {
            log.error("failure to convert cause unexpected code [{}]", dbData, e);
            throw e;
        }
    }
}
