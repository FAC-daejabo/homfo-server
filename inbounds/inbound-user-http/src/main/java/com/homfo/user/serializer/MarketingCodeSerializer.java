package com.homfo.user.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.homfo.enums.MarketingCode;

import java.io.IOException;

/**
 * MarketingCode 객체를 코드 값으로 변경합니다.
 * */
public class MarketingCodeSerializer extends StdSerializer<MarketingCode> {

    public MarketingCodeSerializer() {
        super(MarketingCode.class);
    }

    @Override
    public void serialize(MarketingCode value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(value.getCode());
    }
}
