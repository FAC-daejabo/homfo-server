package com.homfo.employee.deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.homfo.enums.Gender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class GenderDeserializerTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("JSON 문자열에서 Gender 객체로 올바르게 역직렬화되어야 한다.")
    void genderShouldBeDeserializedFromJsonString() throws IOException {
        // given
        String json = "\"M\"";
        mapper.registerModule(new SimpleModule().addDeserializer(Gender.class, new GenderDeserializer()));

        // when
        Gender gender = mapper.readValue(json, Gender.class);

        // then
        assertThat(gender)
                .isNotNull()
                .isEqualTo(Gender.MAN);
    }
}
