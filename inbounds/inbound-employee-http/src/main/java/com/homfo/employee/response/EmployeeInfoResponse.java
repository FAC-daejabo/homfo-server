package com.homfo.employee.response;

import com.homfo.user.dto.EmployeeDto;
import com.homfo.user.infra.enums.EmployeeRole;
import com.homfo.user.infra.enums.EmployeeStatus;
import com.homfo.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 사용자 정보 DTO 입니다.
 * <p>
 * 비밀번호는 절대로 넣지 않습니다.
 */
@Getter
public class EmployeeInfoResponse {
    @Schema(example = "1", description = "ID 값")
    private final Long id;

    @Schema(example = "testAccount", description = "계정")
    private final String account;

    @Schema(example = "닉네임", description = "닉네임")
    private final String nickname;

    @Schema(example = "010-1234-1234", description = "전화번호")
    private final String phoneNumber;

    @Schema(example = "M", description = "성별")
    private final Gender gender;

    @Schema(example = "학생", description = "직업")
    private final String job;

    @Schema(example = "2000-12-12", description = "생년월일")
    private final LocalDate birthday;

    @Schema(example = "U", description = "직원 계정 상태")
    private final EmployeeStatus status;

    @Schema(example = "직원", description = "권한")
    private final EmployeeRole role;

    public EmployeeInfoResponse(EmployeeDto dto) {
        this.id = dto.id();
        this.account = dto.account();
        this.nickname = dto.nickname();
        this.phoneNumber = dto.phoneNumber();
        this.gender = dto.gender();
        this.job = dto.job();
        this.birthday = dto.birthday();
        this.status = dto.status();
        this.role = dto.role();
    }
}
