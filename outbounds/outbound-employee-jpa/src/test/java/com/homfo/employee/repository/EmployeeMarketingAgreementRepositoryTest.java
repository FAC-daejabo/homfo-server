package com.homfo.employee.repository;

import com.homfo.employee.entity.JpaEmployeeMarketingAgreement;
import com.homfo.enums.MarketingCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@EnableAutoConfiguration
@EntityScan("com.homfo.employee.entity")
@ContextConfiguration(classes = {EmployeeMarketingAgreementRepository.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yaml")
class EmployeeMarketingAgreementRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmployeeMarketingAgreementRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${sql.ddl}")
    private String sqlDdl;

    @BeforeEach
    public void setUp() {
        // SQL DDL 실행
        jdbcTemplate.execute(sqlDdl);
    }

    @Test
    void findByUserId_ShouldReturnAgreementsForUser() {
        // given
        Long employeeId = 1L;
        JpaEmployeeMarketingAgreement agreement = JpaEmployeeMarketingAgreement.builder()
                .employeeId(employeeId)
                .code(MarketingCode.SEND_INFORMATION_TO_THIRD_PARTY)
                .agreement(true)
                .build();

        entityManager.persist(agreement);
        entityManager.flush();

        // when
        List<JpaEmployeeMarketingAgreement> foundAgreements = repository.findByEmployeeId(employeeId);

        // then
        assertThat(foundAgreements).hasSize(1);
        assertThat(foundAgreements.get(0).getEmployeeId()).isEqualTo(employeeId);
        assertThat(foundAgreements.get(0).isAgreement()).isTrue();
    }
}
