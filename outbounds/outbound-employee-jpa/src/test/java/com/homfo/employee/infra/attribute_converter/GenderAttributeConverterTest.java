package com.homfo.employee.infra.attribute_converter;

import com.homfo.enums.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

class GenderAttributeConverterTest {

    private GenderAttributeConverter converter;

    @BeforeEach
    void setUp() {
        converter = new GenderAttributeConverter();
    }

    @Test
    @DisplayName("Gender 열거형을 DB 컬럼 문자열로 변환한다")
    void givenGenderEnum_whenConvertToDatabaseColumn_thenReturnsString() {
        // Given
        List<Gender> genderList = Arrays.stream(Gender.values()).toList();

        // When
        List<String> resultList = genderList.stream().map(e -> converter.convertToDatabaseColumn(e)).toList();

        // Then
        for (int i = 0; i < resultList.size(); i++) {
            assertThat(resultList.get(i)).isEqualTo(genderList.get(i).getCode());
        }
    }

    @Test
    @DisplayName("DB 컬럼 문자열을 Gender 열거형으로 변환한다")
    void givenDbColumnString_whenConvertToEntityAttribute_thenReturnsGenderEnum() {
        // Given
        List<String> genderList = Arrays.stream(Gender.values()).map(Gender::getCode).toList();

        // When
        List<Gender> resultList = genderList.stream().map(e -> converter.convertToEntityAttribute(e)).toList();

        // Then
        for (int i = 0; i < resultList.size(); i++) {
            assertThat(resultList.get(i).getCode()).isEqualTo(genderList.get(i));
        }
    }

    @Test
    @DisplayName("잘못된 DB 컬럼 문자열 변환 시 Null을 반환한다")
    void givenInvalidDbColumnString_whenConvertToEntityAttribute_thenThrowsException() {
        // Given
        String invalidCode = "Invalid";

        // When
        Gender result = converter.convertToEntityAttribute(invalidCode);

        // Then
        assertNull(result);
    }
}
