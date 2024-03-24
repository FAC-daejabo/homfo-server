package com.homfo.sms.repository;

import com.homfo.sms.entity.JpaSmsCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SmsCodeRepository extends JpaRepository<JpaSmsCode, String> {
    Optional<JpaSmsCode> findByPhoneNumberAndCode(String phoneNumber, String code);
}
