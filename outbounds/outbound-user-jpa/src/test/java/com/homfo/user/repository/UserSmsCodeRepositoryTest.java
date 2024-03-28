package com.homfo.user.repository;

import com.homfo.user.entity.JpaUserSmsCode;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@EnableAutoConfiguration
@EntityScan("com.homfo.user.entity")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {UserSmsCodeRepository.class})
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yaml")
class UserSmsCodeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserSmsCodeRepository repository;

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
    @Description("전화번호랑 인증 코드로 찾으면 정보를 반환해야한다.")
    void findByPhoneNumberAndCode_ReturnsSmsCode() {
        // given
        String givenPhoneNumber = "010-3868-8634";
        JpaUserSmsCode smsCode = new JpaUserSmsCode(givenPhoneNumber);

        smsCode.createCode();

        entityManager.persistAndFlush(smsCode);

        // when
        Optional<JpaUserSmsCode> foundSmsCode = repository.findByPhoneNumberAndCode(smsCode.getPhoneNumber(), smsCode.getCode());

        // then
        assertThat(foundSmsCode).isPresent();
        assertThat(foundSmsCode.get().getPhoneNumber()).isEqualTo(smsCode.getPhoneNumber());
        assertThat(foundSmsCode.get().getCode()).isEqualTo(smsCode.getCode());
    }

    @Test
    @Description("전화번호랑 인증 번호 상태로 찾으면 정보를 반환해야한다.")
    void findByPhoneNumberAndStatus_ReturnsSmsCode() {
        // given
        String givenPhoneNumber = "010-3868-8634";
        JpaUserSmsCode smsCode = new JpaUserSmsCode(givenPhoneNumber);

        smsCode.createCode();

        entityManager.persistAndFlush(smsCode);

        // when
        Optional<JpaUserSmsCode> foundSmsCode = repository.findByPhoneNumberAndStatus(smsCode.getPhoneNumber(), smsCode.getStatus());

        // then
        assertThat(foundSmsCode).isPresent();
        assertThat(foundSmsCode.get().getPhoneNumber()).isEqualTo(smsCode.getPhoneNumber());
        assertThat(foundSmsCode.get().getStatus()).isEqualTo(smsCode.getStatus());
    }

    // TODO: save 이후 find에서 RuntimeException이 발생함. => 대체 왜그럴까요?
//    @Test
//    void testOptimisticLocking() throws InterruptedException {
//        String phoneNumber = "010-1234-5678";
//        JpaUserSmsCode givenSmsCode = new JpaUserSmsCode(phoneNumber);
//        ExecutorService executorService = Executors.newFixedThreadPool(3);
//        CountDownLatch latch = new CountDownLatch(3);
//        AtomicInteger failureCount = new AtomicInteger(0);
//
//        givenSmsCode.createCode();
//        ReflectionTestUtils.setField(givenSmsCode, "createdAt", givenSmsCode.getCreatedAt().minusMinutes(2));
//
//        repository.save(givenSmsCode);
//
//        entityManager.flush();
//
//
//        for (int i = 0; i < 3; i++) {
//            executorService.execute(() -> {
//               try {
//                   JpaUserSmsCode loadedSmsCode = repository.findById(givenSmsCode.getPhoneNumber()).orElseThrow(RuntimeException::new);
//                   loadedSmsCode.createCode();
//                   repository.save(loadedSmsCode);
//                } catch (ObjectOptimisticLockingFailureException e) {
//                    failureCount.getAndIncrement();
//                }
//                latch.countDown();
//            });
//        }
//
//        latch.await();
//        executorService.shutdown();
////
////        JpaUserSmsCode smsCode = repository.findById(phoneNumber).orElseThrow(RuntimeException::new);
////
////        assertThat(smsCode.getCount()).isEqualTo(1);
////        assertThat(failureCount.get()).isEqualTo(2);
//    }
}
