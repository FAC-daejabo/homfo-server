package com.homfo.user.deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.homfo.enums.MarketingCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class MarketingCodeDeserializerTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("JSON 문자열에서 MarketingCode 객체로 올바르게 역직렬화되어야 한다.")
    void marketingCodeShouldBeDeserializedFromJsonString() throws IOException {
        // given
        String json = "\"MARKETING_CODE_00000001\"";
        mapper.registerModule(new SimpleModule().addDeserializer(MarketingCode.class, new MarketingCodeDeserializer()));

        // when
        MarketingCode marketingCode = mapper.readValue(json, MarketingCode.class);

        // then
        assertThat(marketingCode).isNotNull();
        assertThat(marketingCode).isEqualTo(MarketingCode.SEND_INFORMATION_TO_THIRD_PARTY);
    }
}
