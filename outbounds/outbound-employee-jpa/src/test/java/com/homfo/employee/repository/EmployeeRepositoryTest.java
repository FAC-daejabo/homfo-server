package com.homfo.employee.repository;

import com.homfo.employee.entity.JpaEmployee;
import com.homfo.employee.infra.enums.EmployeeStatus;
import com.homfo.enums.Gender;
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
@EntityScan("com.homfo.employee.entity")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {EmployeeRepository.class})
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yaml")
public class EmployeeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmployeeRepository employeeRepository;

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
    void findByAccount_ReturnsEmployee() {
        // given
        JpaEmployee employee = JpaEmployee.builder()
                .account("testEmployee1")
                .password(encoder.encode("Password@123"))
                .phoneNumber("010-1234-5671")
                .nickname("nickname1")
                .gender(Gender.MAN)
                .birthday(LocalDate.of(1990, 1, 1))
                .job("Developer")
                .build();
        entityManager.persistAndFlush(employee);

        // when
        Optional<JpaEmployee> foundEmployee = employeeRepository.findByAccount(employee.getAccount());

        // then
        assertThat(foundEmployee).isPresent();
        assertThat(foundEmployee.get().getAccount()).isEqualTo(employee.getAccount());
    }

    @Test
    @Description("계정, 닉네임 또는 전화번호로 사용자를 찾으면 해당 사용자를 반환해야 한다.")
    void findByAccountOrNicknameOrPhoneNumber_ReturnsEmployee() {
        // given
        JpaEmployee employee = JpaEmployee.builder()
                .account("testEmployee2")
                .password(encoder.encode("Password@123"))
                .phoneNumber("010-1234-5672")
                .nickname("nickname2")
                .gender(Gender.MAN)
                .birthday(LocalDate.of(1990, 1, 1))
                .job("Developer")
                .build();
        entityManager.persistAndFlush(employee);

        // when
        Optional<JpaEmployee> foundEmployee = employeeRepository.findByAccountOrNicknameOrPhoneNumber(employee.getAccount(), employee.getNickname(), employee.getPhoneNumber());

        // then
        assertThat(foundEmployee).isPresent();
        assertThat(foundEmployee.get().getAccount()).isEqualTo(employee.getAccount());
    }

    @Test
    @Description("ID와 상태가 일치하지 않는 사용자를 찾으면 해당 사용자를 반환해야 한다.")
    void findByIdAndStatusNot_ReturnsEmployee() {
        // given
        JpaEmployee employee = JpaEmployee.builder()
                .account("testEmployee3")
                .password(encoder.encode("Password@123"))
                .phoneNumber("010-1234-5673")
                .nickname("nickname3")
                .gender(Gender.MAN)
                .birthday(LocalDate.of(1990, 1, 1))
                .job("Developer")
                .build();
        entityManager.persistAndFlush(employee);

        // when
        Optional<JpaEmployee> foundEmployee = employeeRepository.findByIdAndStatusNot(employee.getId(), EmployeeStatus.DELETED);

        // then
        assertThat(foundEmployee).isPresent();
        assertThat(foundEmployee.get().getId()).isEqualTo(employee.getId());
    }
}
