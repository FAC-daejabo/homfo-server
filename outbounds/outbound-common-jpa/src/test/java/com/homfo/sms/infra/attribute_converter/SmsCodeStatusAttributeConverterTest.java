package com.homfo.sms.infra.attribute_converter;

import com.homfo.sms.infra.enums.SmsCodeStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SmsCodeStatusAttributeConverterTest {
    private SmsCodeStatusAttributeConverter converter;

    @BeforeEach
    void setUp() {
        converter = new SmsCodeStatusAttributeConverter();
    }

    @Test
    @DisplayName("SmsCodeStatus 열거형을 DB 컬럼 문자열로 변환한다")
    void givenSmsCodeStatusEnum_whenConvertToDatabaseColumn_thenReturnsString() {
        // Given
        List<SmsCodeStatus> smsCodeStatusList = Arrays.stream(SmsCodeStatus.values()).toList();

        // When
        List<String> resultList = smsCodeStatusList.stream().map(e -> converter.convertToDatabaseColumn(e)).toList();

        // Then
        for (int i = 0; i < resultList.size(); i++) {
            assertThat(resultList.get(i)).isEqualTo(smsCodeStatusList.get(i).getCode());
        }
    }

    @Test
    @DisplayName("DB 컬럼 문자열을 SmsCodeStatus 열거형으로 변환한다")
    void givenDbColumnString_whenConvertToEntityAttribute_thenReturnsSmsCodeStatusEnum() {
        // Given
        List<String> smsCodeStatusList = Arrays.stream(SmsCodeStatus.values()).map(SmsCodeStatus::getCode).toList();

        // When
        List<SmsCodeStatus> resultList = smsCodeStatusList.stream().map(e -> converter.convertToEntityAttribute(e)).toList();

        // Then
        for (int i = 0; i < resultList.size(); i++) {
            assertThat(resultList.get(i).getCode()).isEqualTo(smsCodeStatusList.get(i));
        }
    }

    @Test
    @DisplayName("잘못된 DB 컬럼 문자열 변환 시 Null을 반환한다")
    void givenInvalidDbColumnString_whenConvertToEntityAttribute_thenThrowsException() {
        // Given
        String invalidCode = "Invalid";

        // When
        SmsCodeStatus result = converter.convertToEntityAttribute(invalidCode);

        // Then
        assertNull(result);
    }
}
