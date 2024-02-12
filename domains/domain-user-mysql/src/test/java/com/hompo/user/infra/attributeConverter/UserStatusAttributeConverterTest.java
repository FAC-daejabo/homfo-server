package com.hompo.user.infra.attributeConverter;

import com.hompo.user.infra.enums.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserStatusAttributeConverterTest {

    private UserStatusAttributeConverter converter;

    @BeforeEach
    void setUp() {
        converter = new UserStatusAttributeConverter();
    }

    @Test
    @DisplayName("UserStatus 열거형을 DB 컬럼 문자열로 변환한다")
    void givenUserStatusEnum_whenConvertToDatabaseColumn_thenReturnsString() {
        // Given
        List<UserStatus> userStatusList = Arrays.stream(UserStatus.values()).toList();

        // When
        List<String> resultList = userStatusList.stream().map(e -> converter.convertToDatabaseColumn(e)).toList();

        // Then
        for (int i = 0; i < resultList.size(); i++) {
            assertThat(resultList.get(i)).isEqualTo(userStatusList.get(i).getCode());
        }
    }

    @Test
    @DisplayName("DB 컬럼 문자열을 UserStatus 열거형으로 변환한다")
    void givenDbColumnString_whenConvertToEntityAttribute_thenReturnsUserStatusEnum() {
        // Given
        List<String> userStatusList = Arrays.stream(UserStatus.values()).map(UserStatus::getCode).toList();

        // When
        List<UserStatus> resultList = userStatusList.stream().map(e -> converter.convertToEntityAttribute(e)).toList();

        // Then
        for (int i = 0; i < resultList.size(); i++) {
            assertThat(resultList.get(i).getCode()).isEqualTo(userStatusList.get(i));
        }
    }

    @Test
    @DisplayName("잘못된 DB 컬럼 문자열 변환 시 예외를 발생시킨다")
    void givenInvalidDbColumnString_whenConvertToEntityAttribute_thenThrowsException() {
        // Given
        String invalidCode = "Invalid";

        // When
        UserStatus result = converter.convertToEntityAttribute(invalidCode);

        // Then
        assertNull(result);
    }
}
