package com.hompo.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.hompo.enums.Gender;

import java.io.IOException;

/**
 * Gender 객체를 코드 값으로 변경합니다.
 * */
public class GenderSerializer extends StdSerializer<Gender> {

    public GenderSerializer() {
        super(Gender.class);
    }

    @Override
    public void serialize(Gender value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(value.getCode());
    }
}
