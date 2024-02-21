package com.homfo.user.infra.enums;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Description;

import static org.assertj.core.api.Assertions.assertThat;

public class UserStatusTest {

    @Test
    @Description("코드로부터 UserStatus를 생성할 수 있어야 한다.")
    void createUserStatusFromCode() {
        // given
        String useCode = "U";
        String deletedCode = "D";
        String stoppedCode = "S";

        // when
        UserStatus useStatus = UserStatus.fromCode(useCode);
        UserStatus deletedStatus = UserStatus.fromCode(deletedCode);
        UserStatus stoppedStatus = UserStatus.fromCode(stoppedCode);

        // then
        assertThat(useStatus).isEqualTo(UserStatus.USE);
        assertThat(deletedStatus).isEqualTo(UserStatus.DELETED);
        assertThat(stoppedStatus).isEqualTo(UserStatus.STOPPED);
    }

    @Test
    @Description("사용자가 삭제된 상태인지 확인할 수 있어야 한다.")
    void checkIfUserStatusIsDeleted() {
        // given
        UserStatus status = UserStatus.DELETED;

        // when
        boolean isDeleted = status.isDeleted();

        // then
        assertThat(isDeleted).isTrue();
    }

    @Test
    @Description("사용자가 정지된 상태인지 확인할 수 있어야 한다.")
    void checkIfUserStatusIsStopped() {
        // given
        UserStatus status = UserStatus.STOPPED;

        // when
        boolean isStopped = status.isStopped();

        // then
        assertThat(isStopped).isTrue();
    }
}
