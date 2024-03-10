package com.homfo.employee.infra.enums;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Description;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeStatusTest {

    @Test
    @Description("코드로부터 EmployeeStatus를 생성할 수 있어야 한다.")
    void createEmployeeStatusFromCode() {
        // given
        String useCode = "U";
        String deletedCode = "D";
        String stoppedCode = "S";
        String pendingCode = "P";

        // when
        EmployeeStatus useStatus = EmployeeStatus.fromCode(useCode);
        EmployeeStatus deletedStatus = EmployeeStatus.fromCode(deletedCode);
        EmployeeStatus stoppedStatus = EmployeeStatus.fromCode(stoppedCode);
        EmployeeStatus pendingStatus = EmployeeStatus.fromCode(pendingCode);

        // then
        assertThat(useStatus).isEqualTo(EmployeeStatus.USE);
        assertThat(deletedStatus).isEqualTo(EmployeeStatus.DELETED);
        assertThat(stoppedStatus).isEqualTo(EmployeeStatus.STOPPED);
        assertThat(pendingStatus).isEqualTo(EmployeeStatus.PENDING);
    }

    @Test
    @Description("사용자가 삭제된 상태인지 확인할 수 있어야 한다.")
    void checkIfEmployeeStatusIsDeleted() {
        // given
        EmployeeStatus status = EmployeeStatus.DELETED;

        // when
        boolean isDeleted = status.isDeleted();

        // then
        assertThat(isDeleted).isTrue();
    }

    @Test
    @Description("사용자가 정지된 상태인지 확인할 수 있어야 한다.")
    void checkIfEmployeeStatusIsStopped() {
        // given
        EmployeeStatus status = EmployeeStatus.STOPPED;

        // when
        boolean isStopped = status.isStopped();

        // then
        assertThat(isStopped).isTrue();
    }
}
