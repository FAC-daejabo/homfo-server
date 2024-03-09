package com.homfo.user.dto;

import com.homfo.enums.Gender;
import com.homfo.user.infra.enums.UserStatus;

import java.time.LocalDate;

/**
 * 사용자 정보 DTO 입니다.
 *
 * 비밀번호는 절대로 넣지 않습니다.
 * */
public record UserDto(

        Long id,

        String account,

        String nickname,

        String phoneNumber,

        Gender gender,

        String job,

        LocalDate birthday,

        UserStatus status
) {
}
