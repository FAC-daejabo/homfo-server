package com.hompo.user.repository;

import com.hompo.enums.MarketingCode;
import com.hompo.user.entity.MySqlUserMarketingAgreement;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@EnableAutoConfiguration
@EntityScan("com.hompo.user.entity")
@ContextConfiguration(classes = {UserMarketingAgreementRepository.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@ActiveProfiles("test")
public class UserMarketingAgreementRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserMarketingAgreementRepository repository;

    @Test
    void findByUserId_ShouldReturnAgreementsForUser() {
        // given
        Long userId = 1L;
        MySqlUserMarketingAgreement agreement = MySqlUserMarketingAgreement.builder()
                .userId(userId)
                .code(MarketingCode.sendInformationToThirdParty)
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
