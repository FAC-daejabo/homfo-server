//package com.hompo.user.repository;
//
//import com.hompo.enums.MarketingCode;
//import com.hompo.user.entity.MySqlUserMarketingAgreement;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.context.annotation.ComponentScan;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest
//@ComponentScan(basePackages = {"com.hompo"})
//public class UserMarketingAgreementRepositoryTest {
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Autowired
//    private UserMarketingAgreementRepository repository;
//
//    @Test
//    void findByUserId_ShouldReturnAgreementsForUser() {
//        // given
//        Long userId = 1L;
//        MySqlUserMarketingAgreement agreement = MySqlUserMarketingAgreement.builder()
//                .userId(userId)
//                .code(MarketingCode.sendInformationToThirdParty)
//                .agreement(true)
//                .build();
//
//        entityManager.persist(agreement);
//        entityManager.flush();
//
//        // when
//        List<MySqlUserMarketingAgreement> foundAgreements = repository.findByUserId(userId);
//
//        // then
//        assertThat(foundAgreements).hasSize(1);
//        assertThat(foundAgreements.get(0).getUserId()).isEqualTo(userId);
//        assertThat(foundAgreements.get(0).isAgreement()).isTrue();
//    }
//}
