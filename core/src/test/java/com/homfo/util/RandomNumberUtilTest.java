package com.homfo.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomNumberUtilTest {

    @Test
    @DisplayName("요청한 길이의 랜덤 숫자 문자열을 생성한다")
     void testRandomLength() {
        // given: 요청된 길이
        int requestedLength = 6;

        // when: RandomNumberUtil을 사용하여 랜덤 숫자 문자열을 생성
        String randomNumberStr = RandomNumberUtil.random(requestedLength);

        // then: 반환된 문자열의 길이를 검증
        assertEquals(requestedLength, randomNumberStr.length(), "생성된 문자열은 요청한 길이를 가져야 한다.");
    }

    @Test
    @DisplayName("생성된 랜덤 숫자 문자열이 숫자로만 구성되어 있다")
     void testRandomNumericContent() {
        // given: 생성을 위한 요청 (특별한 조건 없음)

        // when: 랜덤 숫자 문자열 생성
        String randomNumberStr = RandomNumberUtil.random(8);

        // then: 반환된 문자열이 숫자로만 구성되어 있는지 검증
        assertTrue(randomNumberStr.matches("\\d+"), "생성된 문자열은 숫자로만 구성되어야 한다.");
    }
}
