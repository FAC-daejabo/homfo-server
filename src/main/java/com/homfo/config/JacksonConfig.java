package com.homfo.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.homfo.enums.Gender;
import com.homfo.enums.MarketingCode;
import com.homfo.deserializer.GenderDeserializer;
import com.homfo.deserializer.MarketingCodeDeserializer;
import com.homfo.serializer.GenderSerializer;
import com.homfo.serializer.MarketingCodeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper () {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();

        // 객체를 JSON으로 변환할 때 들여쓰기를 적용한 형태로 출력합니다.
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        // 2000-12-12를 [2000, 12, 12]와 같은 형식으로 사용하지 않습니다.
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        addSerializers(module);
        addDeserializers(module);

        mapper.registerModule(module);
        mapper.registerModule(new JavaTimeModule());

        return mapper;
    }

    /**
     * Custom Serializer를 등록합니다.
     *
     * 내부 객체를 외부 데이터 포맷으로 변경합니다.
     * */
    private static void addSerializers(SimpleModule module) {
        module.addSerializer(Gender.class, new GenderSerializer());
        module.addSerializer(MarketingCode.class, new MarketingCodeSerializer());
    }

    /**
     * Custom Deserializer를 등록합니다.
     *
     * 외부 데이터 포맷을 내부 객체로 변환합니다.
     * */
    private static void addDeserializers(SimpleModule module) {
        module.addDeserializer(Gender.class, new GenderDeserializer());
        module.addDeserializer(MarketingCode.class, new MarketingCodeDeserializer());
    }
}