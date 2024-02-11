package com.hompo.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hompo.deserializer.GenderDeserializer;
import com.hompo.deserializer.MarketingCodeDeserializer;
import com.hompo.enums.Gender;
import com.hompo.enums.MarketingCode;
import com.hompo.serializer.GenderSerializer;
import com.hompo.serializer.MarketingCodeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.Map;

@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper () {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();

        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        addSerializers(module);
        addDeserializers(module);

        mapper.registerModule(module);
        mapper.registerModule(new JavaTimeModule());

        return mapper;
    }

    private static void addSerializers(SimpleModule module) {
        module.addSerializer(Gender.class, new GenderSerializer());
        module.addSerializer(MarketingCode.class, new MarketingCodeSerializer());
    }

    private static void addDeserializers(SimpleModule module) {
        module.addDeserializer(Gender.class, new GenderDeserializer());
        module.addDeserializer(MarketingCode.class, new MarketingCodeDeserializer());
    }
}