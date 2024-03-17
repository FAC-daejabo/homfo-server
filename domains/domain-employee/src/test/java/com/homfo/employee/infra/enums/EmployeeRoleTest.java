package com.homfo.employee.infra.enums;

import com.homfo.employee.infra.enums.EmployeeRole;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Description;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeRoleTest {

    @Test
    @Description("코드로부터 EmployeeRole를 생성할 수 있어야 한다.")
    void createEmployeeRoleFromCode() {
        // given
        String employeeCode = "직원";
        String adminCode = "관리자";

        // when
        EmployeeRole employeeRole = EmployeeRole.fromCode(employeeCode);
        EmployeeRole adminRole = EmployeeRole.fromCode(adminCode);

        // then
        assertThat(employeeRole).isEqualTo(EmployeeRole.EMPLOYEE);
        assertThat(adminRole).isEqualTo(EmployeeRole.ADMIN);
    }
}
