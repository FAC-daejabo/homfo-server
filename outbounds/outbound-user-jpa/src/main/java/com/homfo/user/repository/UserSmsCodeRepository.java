package com.homfo.user.repository;

import com.homfo.sms.infra.enums.SmsCodeStatus;
import com.homfo.user.entity.JpaUserSmsCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSmsCodeRepository extends JpaRepository<JpaUserSmsCode, String> {
    Optional<JpaUserSmsCode> findByPhoneNumberAndCode(String phoneNumber, String code);

    Optional<JpaUserSmsCode> findByPhoneNumberAndStatus(String phoneNumber, SmsCodeStatus status);
}
