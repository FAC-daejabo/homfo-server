package com.homfo.employee.attribute_converter;

import com.homfo.enums.MarketingCode;
import com.homfo.employee.attribute_converter.MarketingCodeAttributeConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

class MarketingCodeAttributeConverterTest {

    private MarketingCodeAttributeConverter converter;

    @BeforeEach
    void setUp() {
        converter = new MarketingCodeAttributeConverter();
    }

    @Test
    @DisplayName("MarketingCode 열거형을 DB 컬럼 문자열로 변환한다")
    void givenMarketingCodeEnum_whenConvertToDatabaseColumn_thenReturnsString() {
        // Given
        List<MarketingCode> marketingCodeList = Arrays.stream(MarketingCode.values()).toList();

        // When
        List<String> resultList = marketingCodeList.stream().map(e -> converter.convertToDatabaseColumn(e)).toList();

        // Then
        for (int i = 0; i < resultList.size(); i++) {
            assertThat(resultList.get(i)).isEqualTo(marketingCodeList.get(i).getCode());
        }
    }

    @Test
    @DisplayName("DB 컬럼 문자열을 MarketingCode 열거형으로 변환한다")
    void givenDbColumnString_whenConvertToEntityAttribute_thenReturnsMarketingCodeEnum() {
        // Given
        List<String> marketingCodeList = Arrays.stream(MarketingCode.values()).map(MarketingCode::getCode).toList();

        // When
        List<MarketingCode> resultList = marketingCodeList.stream().map(e -> converter.convertToEntityAttribute(e)).toList();

        // Then
        for (int i = 0; i < resultList.size(); i++) {
            assertThat(resultList.get(i).getCode()).isEqualTo(marketingCodeList.get(i));
        }
    }

    @Test
    @DisplayName("잘못된 DB 컬럼 문자열 변환 시 Null을 반환한다")
    void givenInvalidDbColumnString_whenConvertToEntityAttribute_thenThrowsException() {
        // Given
        String invalidCode = "Invalid";

        // When
        MarketingCode result = converter.convertToEntityAttribute(invalidCode);

        // Then
        assertNull(result);
    }
}
