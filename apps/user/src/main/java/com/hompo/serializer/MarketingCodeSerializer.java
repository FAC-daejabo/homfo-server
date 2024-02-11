package com.hompo.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.hompo.enums.Gender;
import com.hompo.enums.MarketingCode;

import java.io.IOException;

public class MarketingCodeSerializer extends StdSerializer<MarketingCode> {

    public MarketingCodeSerializer() {
        super(MarketingCode.class);
    }

    @Override
    public void serialize(MarketingCode value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(value.getCode());
    }
}
