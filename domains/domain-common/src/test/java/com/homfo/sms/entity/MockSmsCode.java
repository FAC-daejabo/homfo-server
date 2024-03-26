package com.homfo.sms.entity;

import com.homfo.sms.infra.enums.SmsCodeStatus;

import java.time.LocalDateTime;

public class MockSmsCode extends SmsCode {
    private String phoneNumber;
    private LocalDateTime updatedAt;
    private Long version;


    public MockSmsCode() {
        this.count = 0;
        this.firstCreatedAt = LocalDateTime.now();
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

    public void setFirstCreatedAt(LocalDateTime firstCreatedAt) {
        this.firstCreatedAt = firstCreatedAt;
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
}
