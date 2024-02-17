package com.homfo.enums;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GenderTest {
    @Test
    void fromCode_WithValidCode_ShouldNotNull() {
        // given
        List<String> validCodes = new ArrayList<>(List.of("M", "W"));

        // when
        for (String validCode : validCodes) {
            // when
            Gender gender = Gender.fromCode(validCode);

            // then
            assertNotNull(gender);
        }
    }

    @Test
    void fromCode_WithInvalidCode_ShouldNull() {
        // given
        List<String> invalidCodes = new ArrayList<>(List.of("abc"));

        // when
        for (String invalidCode : invalidCodes) {
            // when
            Gender gender = Gender.fromCode(invalidCode);

            // then
            assertNull(gender);
        }
    }
}
