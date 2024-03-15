package com.homfo.user.repository;

import com.homfo.enums.Gender;
import com.homfo.user.entity.JpaUser;
import com.homfo.user.infra.enums.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Description;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@EnableAutoConfiguration
@EntityScan("com.homfo.user.entity")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {UserRepository.class})
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yaml")
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${sql.ddl}")
    private String sqlDdl;

    private PasswordEncoder encoder;

    @BeforeEach
    public void setUp() {
        encoder = new BCryptPasswordEncoder();

        // SQL DDL 실행
        jdbcTemplate.execute(sqlDdl);
    }

    @Test
    @Description("계정으로 사용자를 찾으면 해당 사용자를 반환해야 한다.")
    void findByAccount_ReturnsUser() {
        // given
        JpaUser user = JpaUser.builder()
                .account("testUser1")
                .password(encoder.encode("Password@123"))
                .phoneNumber("010-1234-5671")
                .nickname("nickname1")
                .gender(Gender.MAN)
                .birthday(LocalDate.of(1990, 1, 1))
                .job("Developer")
                .build();
        entityManager.persistAndFlush(user);

        // when
        Optional<JpaUser> foundUser = userRepository.findByAccount(user.getAccount());

        // then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getAccount()).isEqualTo(user.getAccount());
    }

    @Test
    @Description("계정, 닉네임 또는 전화번호로 사용자를 찾으면 해당 사용자를 반환해야 한다.")
    void findByAccountOrNicknameOrPhoneNumber_ReturnsUser() {
        // given
        JpaUser user = JpaUser.builder()
                .account("testUser2")
                .password(encoder.encode("Password@123"))
                .phoneNumber("010-1234-5672")
                .nickname("nickname2")
                .gender(Gender.MAN)
                .birthday(LocalDate.of(1990, 1, 1))
                .job("Developer")
                .build();
        entityManager.persistAndFlush(user);

        // when
        Optional<JpaUser> foundUser = userRepository.findByAccountOrNicknameOrPhoneNumber(user.getAccount(), user.getNickname(), user.getPhoneNumber());

        // then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getAccount()).isEqualTo(user.getAccount());
    }

    @Test
    @Description("ID와 상태가 일치하지 않는 사용자를 찾으면 해당 사용자를 반환해야 한다.")
    void findByIdAndStatusNot_ReturnsUser() {
        // given
        JpaUser user = JpaUser.builder()
                .account("testUser3")
                .password(encoder.encode("Password@123"))
                .phoneNumber("010-1234-5673")
                .nickname("nickname3")
                .gender(Gender.MAN)
                .birthday(LocalDate.of(1990, 1, 1))
                .job("Developer")
                .build();
        entityManager.persistAndFlush(user);

        // when
        Optional<JpaUser> foundUser = userRepository.findByIdAndStatusNot(user.getId(), UserStatus.DELETED);

        // then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getId()).isEqualTo(user.getId());
    }
}
