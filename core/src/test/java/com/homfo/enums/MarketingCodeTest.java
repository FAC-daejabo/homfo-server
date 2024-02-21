package com.homfo.enums;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class MarketingCodeTest {
    @Test
    void fromCode_WithValidCode_ShouldNotNull() {
        // given
        List<String> validCodes = new ArrayList<>(List.of("MARKETING_CODE_00000001"));

        // when
        for (String validCode : validCodes) {
            // when
            MarketingCode marketingCode = MarketingCode.fromCode(validCode);

            // then
            assertNotNull(marketingCode);
        }
    }

    @Test
    void fromCode_WithInvalidCode_ShouldNull() {
        // given
        List<String> invalidCodes = new ArrayList<>(List.of("abc"));

        // when
        for (String invalidCode : invalidCodes) {
            // when
            MarketingCode marketingCode = MarketingCode.fromCode(invalidCode);

            // then
            assertNull(marketingCode);
        }
    }
}
