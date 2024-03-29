package com.homfo.employee.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.homfo.enums.MarketingCode;

import java.io.IOException;


/**
 * MarketingCode의 코드 값을 객체로 변환합니다.
 * */
public class MarketingCodeDeserializer extends JsonDeserializer<MarketingCode> {
 
    @Override
    public MarketingCode deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String code = p.getText();
        return MarketingCode.fromCode(code);
    }
}
