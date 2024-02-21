package com.homfo.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.homfo.enums.Gender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;

class GenderSerializerTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("Gender 객체가 코드 값으로 올바르게 직렬화되어야 한다.")
    void genderShouldBeSerializedToCode() throws JsonProcessingException {
        // given
        Gender gender = Gender.MAN;
        mapper.registerModule(new SimpleModule().addSerializer(Gender.class, new GenderSerializer()));

        // when
        String serializedGender = mapper.writeValueAsString(gender);

        // then
        assertThat(serializedGender).isEqualTo("\"" + gender.getCode() + "\"");
    }
}
