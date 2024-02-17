package com.homfo.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.homfo.enums.Gender;

import java.io.IOException;

/**
 * Gender code 값을 객체로 변환합니다.
 * */
public class GenderDeserializer extends JsonDeserializer<Gender> {

    @Override
    public Gender deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String code = p.getText();
        return Gender.fromCode(code);
    }
}
