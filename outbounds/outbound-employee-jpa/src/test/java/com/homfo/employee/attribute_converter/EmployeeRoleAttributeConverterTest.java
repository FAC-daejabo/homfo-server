package com.homfo.employee.attribute_converter;

import com.homfo.employee.infra.enums.EmployeeRole;
import com.homfo.employee.attribute_converter.EmployeeRoleAttributeConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

class EmployeeRoleAttributeConverterTest {

    private EmployeeRoleAttributeConverter converter;

    @BeforeEach
    void setUp() {
        converter = new EmployeeRoleAttributeConverter();
    }

    @Test
    @DisplayName("EmployeeRole 열거형을 DB 컬럼 문자열로 변환한다")
    void givenEmployeeRoleEnum_whenConvertToDatabaseColumn_thenReturnsString() {
        // Given
        List<EmployeeRole> employeeRoleList = Arrays.stream(EmployeeRole.values()).toList();

        // When
        List<String> resultList = employeeRoleList.stream().map(e -> converter.convertToDatabaseColumn(e)).toList();

        // Then
        for (int i = 0; i < resultList.size(); i++) {
            assertThat(resultList.get(i)).isEqualTo(employeeRoleList.get(i).getCode());
        }
    }

    @Test
    @DisplayName("DB 컬럼 문자열을 EmployeeRole 열거형으로 변환한다")
    void givenDbColumnString_whenConvertToEntityAttribute_thenReturnsEmployeeRoleEnum() {
        // Given
        List<String> employeeRoleList = Arrays.stream(EmployeeRole.values()).map(EmployeeRole::getCode).toList();

        // When
        List<EmployeeRole> resultList = employeeRoleList.stream().map(e -> converter.convertToEntityAttribute(e)).toList();

        // Then
        for (int i = 0; i < resultList.size(); i++) {
            assertThat(resultList.get(i).getCode()).isEqualTo(employeeRoleList.get(i));
        }
    }

    @Test
    @DisplayName("잘못된 DB 컬럼 문자열 변환 시 예외를 발생시킨다")
    void givenInvalidDbColumnString_whenConvertToEntityAttribute_thenThrowsException() {
        // Given
        String invalidCode = "Invalid";

        // When
        EmployeeRole result = converter.convertToEntityAttribute(invalidCode);

        // Then
        assertNull(result);
    }
}
