package com.homfo.user.infra.util;

public class ValidationUtil {

    // 비밀번호 정규식: 최소 8자 이상, 최대 15자 이상, 숫자, 대소문자 알파벳, 특수문자 각각 최소 하나 이상 포함
    private static final String PASSWORD_PATTERN = 
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,15}$";

    /**
     * 사용자의 원본 비밀번호가 정규식 조건을 만족하는지 검증합니다.
     * 
     * @param password 검증할 비밀번호 문자열
     * @throws IllegalArgumentException 비밀번호가 정규식 조건을 만족하지 않을 경우
     */
    public static void validateUserOriginPassword(String password) {
        if (password == null || !password.matches(PASSWORD_PATTERN)) {
            throw new IllegalArgumentException("비밀번호는 최소 8자 이상이며, 숫자, 대소문자 알파벳, 특수문자를 각각 하나 이상 포함해야 합니다.");
        }
    }
}
