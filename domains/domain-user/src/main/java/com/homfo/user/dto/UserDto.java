package com.homfo.user.dto;

import com.homfo.enums.Gender;
import com.homfo.user.infra.enums.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

/**
 * 사용자 정보 DTO 입니다.
 *
 * 비밀번호는 절대로 넣지 않습니다.
 * */
public record UserDto(
        @Schema(example = "1", description = "ID 값")
        Long id,

        @Schema(example = "testAccount", description = "계정")
        String account,

        @Schema(example = "닉네임", description = "닉네임")
        String nickname,

        @Schema(example = "010-1234-1234", description = "전화번호")
        String phoneNumber,

        @Schema(example = "M", description = "성별")
        Gender gender,

        @Schema(example = "학생", description = "직업")
        String job,

        @Schema(example = "2000-12-12", description = "생년월일")
        LocalDate birthday,

        @Schema(example = "U", description = "사용자 계정 상태")
        UserStatus status
) {
}
