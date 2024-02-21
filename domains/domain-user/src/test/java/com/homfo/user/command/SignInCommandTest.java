package com.homfo.user.command;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;

 class SignInCommandTest {

    @Test
    @DisplayName("로그인 커맨드가 올바르게 생성되어야 한다.")
    void signInCommandIsCreatedProperly() {
        // given
        String account = "testAccount";
        String password = "testPW@111";

        // when
        SignInCommand command = new SignInCommand(account, password);

        // then
        assertThat(command.account()).isEqualTo(account);
        assertThat(command.password()).isEqualTo(password);
    }
}
