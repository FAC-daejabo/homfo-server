package com.homfo.user.dto;

import com.homfo.user.infra.enums.EmployeeRole;
import com.homfo.user.infra.enums.EmployeeStatus;
import com.homfo.enums.Gender;

import java.time.LocalDate;

/**
 * 직원 정보 DTO 입니다.
 *
 * 비밀번호는 절대로 넣지 않습니다.
 * */
public record EmployeeDto(

        Long id,

        String account,

        String nickname,

        String phoneNumber,

        Gender gender,

        String job,

        LocalDate birthday,

        EmployeeStatus status,

        EmployeeRole role
) {
}
