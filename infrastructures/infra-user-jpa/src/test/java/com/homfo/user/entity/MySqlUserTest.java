package com.homfo.user.entity;

import com.homfo.enums.Gender;
import com.homfo.user.infra.enums.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class MySqlUserTest {

    private MySqlUser user;
    private PasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        encoder = new BCryptPasswordEncoder();
        user = MySqlUser.builder()
                .account("testUser")
                .password(encoder.encode("Password@123"))
                .phoneNumber("010-1234-5678")
                .nickname("nickname")
                .gender(Gender.MAN)
                .birthday(LocalDate.of(1990, 1, 1))
                .job("Developer")
                .build();
    }

    @Test
    void givenValidUserInfo_whenCreatingUser_thenUserIsCreatedSuccessfully() {
        // given: 유효한 사용자 정보
        String account = "testUser";
        String password = encoder.encode("TestPassword123!");
        String phoneNumber = "010-1234-5678";
        String nickname = "testNickname";
        Gender gender = Gender.MAN;
        LocalDate birthday = LocalDate.of(1990, 1, 1);
        String job = "Developer";

        // when: MySqlUser 생성자를 사용하여 인스턴스 생성
        MySqlUser user = MySqlUser.builder()
                .account(account)
                .password(password) // 실제 사용 시에는 암호화된 패스워드가 제공되어야 합니다.
                .phoneNumber(phoneNumber)
                .nickname(nickname)
                .gender(gender)
                .birthday(birthday)
                .job(job)
                .build();

        // then: 생성된 사용자 정보가 기대값과 일치해야 함
        assertThat(user.getAccount()).isEqualTo(account);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(user.getNickname()).isEqualTo(nickname);
        assertThat(user.getGender()).isEqualTo(gender);
        assertThat(user.getBirthday()).isEqualTo(birthday);
        assertThat(user.getJob()).isEqualTo(job);
        assertThat(user.getStatus()).isEqualTo(UserStatus.USE); // PrePersist에 의해 상태가 USE로 설정됨
    }

    @Test
    void givenInvalidUserInfo_whenCreatingUser_thenThrowException() {
        // given: 유효하지 않은 사용자 정보 (예: 비밀번호가 null)
        String account = "testUser";
        String password = null; // 유효하지 않은 값
        String phoneNumber = "010-1234-5678";
        String nickname = "testNickname";
        Gender gender = Gender.MAN;
        LocalDate birthday = LocalDate.of(1990, 1, 1);
        String job = "Developer";

        // when & then: MySqlUser 생성자를 사용하여 인스턴스 생성 시 예외가 발생해야 함
        assertThatThrownBy(() -> MySqlUser.builder()
                .account(account)
                .password(password) // 비밀번호 유효성 검사 실패
                .phoneNumber(phoneNumber)
                .nickname(nickname)
                .gender(gender)
                .birthday(birthday)
                .job(job)
                .build()).isInstanceOf(RuntimeException.class); // TODO: 구체적인 예외 타입으로 변경
    }

    @Test
    void signIn_WithCorrectPassword_ShouldSucceed() {
        // given: 올바른 비밀번호 준비
        String correctPassword = "Password@123";

        // when: signIn 메서드 실행
        Executable action = () -> user.signIn(encoder, correctPassword);

        // then: 예외가 발생하지 않아야 함
        assertDoesNotThrow(action);
    }

    @Test
    void signIn_WithIncorrectPassword_ShouldFail() {
        // given: 잘못된 비밀번호 준비
        String wrongPassword = "wrongPassword";

        // when: signIn 메서드 실행
        Executable action = () -> user.signIn(encoder, wrongPassword);

        // then: RuntimeException 예외 발생
        assertThrows(RuntimeException.class, action);
    }

    @Test
    void deleteAccount_ShouldSetUserStatusToDeleted() {
        // given: 사용자 계정 존재

        // when: deleteAccount 메서드 실행
        user.deleteAccount();

        // then: 사용자 상태가 DELETED로 변경되고, 계정 관련 정보가 null이 되어야 함
        assertEquals(UserStatus.DELETED, user.getStatus());
        assertNull(user.getAccount());
        assertNull(user.getPassword());
        assertNull(user.getPhoneNumber());
        assertNull(user.getNickname());
    }
}
