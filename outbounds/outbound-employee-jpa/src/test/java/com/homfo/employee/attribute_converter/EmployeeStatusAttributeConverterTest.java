package com.homfo.employee.attribute_converter;

import com.homfo.employee.infra.enums.EmployeeStatus;
import com.homfo.employee.attribute_converter.EmployeeStatusAttributeConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

class EmployeeStatusAttributeConverterTest {

    private EmployeeStatusAttributeConverter converter;

    @BeforeEach
    void setUp() {
        converter = new EmployeeStatusAttributeConverter();
    }

    @Test
    @DisplayName("EmployeeStatus 열거형을 DB 컬럼 문자열로 변환한다")
    void givenEmployeeStatusEnum_whenConvertToDatabaseColumn_thenReturnsString() {
        // Given
        List<EmployeeStatus> employeeStatusList = Arrays.stream(EmployeeStatus.values()).toList();

        // When
        List<String> resultList = employeeStatusList.stream().map(e -> converter.convertToDatabaseColumn(e)).toList();

        // Then
        for (int i = 0; i < resultList.size(); i++) {
            assertThat(resultList.get(i)).isEqualTo(employeeStatusList.get(i).getCode());
        }
    }

    @Test
    @DisplayName("DB 컬럼 문자열을 EmployeeStatus 열거형으로 변환한다")
    void givenDbColumnString_whenConvertToEntityAttribute_thenReturnsEmployeeStatusEnum() {
        // Given
        List<String> employeeStatusList = Arrays.stream(EmployeeStatus.values()).map(EmployeeStatus::getCode).toList();

        // When
        List<EmployeeStatus> resultList = employeeStatusList.stream().map(e -> converter.convertToEntityAttribute(e)).toList();

        // Then
        for (int i = 0; i < resultList.size(); i++) {
            assertThat(resultList.get(i).getCode()).isEqualTo(employeeStatusList.get(i));
        }
    }

    @Test
    @DisplayName("잘못된 DB 컬럼 문자열 변환 시 예외를 발생시킨다")
    void givenInvalidDbColumnString_whenConvertToEntityAttribute_thenThrowsException() {
        // Given
        String invalidCode = "Invalid";

        // When
        EmployeeStatus result = converter.convertToEntityAttribute(invalidCode);

        // Then
        assertNull(result);
    }
}
