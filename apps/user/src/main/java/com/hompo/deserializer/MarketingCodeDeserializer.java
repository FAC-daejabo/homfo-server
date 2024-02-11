package com.hompo.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.hompo.enums.Gender;
import com.hompo.enums.MarketingCode;

import java.io.IOException;

public class MarketingCodeDeserializer extends JsonDeserializer<MarketingCode> {

    @Override
    public MarketingCode deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String code = p.getText();
        return MarketingCode.fromCode(code);
    }
}
