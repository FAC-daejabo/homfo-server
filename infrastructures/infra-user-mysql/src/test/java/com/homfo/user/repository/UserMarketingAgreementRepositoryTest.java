package com.homfo.user.repository;

import com.homfo.enums.MarketingCode;
import com.homfo.user.entity.MySqlUserMarketingAgreement;

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
@EntityScan("com.homfo.user.entity")
@ContextConfiguration(classes = {UserMarketingAgreementRepository.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yaml")
class UserMarketingAgreementRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserMarketingAgreementRepository repository;

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
        Long userId = 1L;
        MySqlUserMarketingAgreement agreement = MySqlUserMarketingAgreement.builder()
                .userId(userId)
                .code(MarketingCode.SEND_INFORMATION_TO_THIRD_PARTY)
                .agreement(true)
                .build();

        entityManager.persist(agreement);
        entityManager.flush();

        // when
        List<MySqlUserMarketingAgreement> foundAgreements = repository.findByUserId(userId);

        // then
        assertThat(foundAgreements).hasSize(1);
        assertThat(foundAgreements.get(0).getUserId()).isEqualTo(userId);
        assertThat(foundAgreements.get(0).isAgreement()).isTrue();
    }
}
