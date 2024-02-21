package com.homfo.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.homfo.enums.MarketingCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;

class MarketingCodeSerializerTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("MarketingCode 객체가 코드 값으로 올바르게 직렬화되어야 한다.")
    void marketingCodeShouldBeSerializedToCode() throws JsonProcessingException {
        // given
        MarketingCode code = MarketingCode.SEND_INFORMATION_TO_THIRD_PARTY;
        mapper.registerModule(new SimpleModule().addSerializer(MarketingCode.class, new MarketingCodeSerializer()));

        // when
        String serializedCode = mapper.writeValueAsString(code);

        // then
        assertThat(serializedCode).isEqualTo("\"" + code.getCode() + "\"");
    }
}
