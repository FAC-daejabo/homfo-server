package com.homfo.sms.entity;

import com.homfo.sms.dto.SmsCodeTransactionDto;
import com.homfo.sms.infra.enums.SmsCodeStatus;

import java.time.LocalDateTime;

public class MockSmsCode extends SmsCode {
    private String phoneNumber;
    private LocalDateTime updatedAt;
    private Long version;


    public MockSmsCode() {
        this.count = 0;
        this.createdAt = LocalDateTime.now();
    }
    public void setCode(String code) {
        this.code = code;
    }

    public void setStatus(SmsCodeStatus status) {
        this.status = status;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public void rollback(SmsCodeTransactionDto smsCodeTransactionDto) {

    }
}
