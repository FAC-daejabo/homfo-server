package com.homfo.user.entity;

import com.homfo.sms.dto.SmsCodeTransactionDto;
import com.homfo.sms.entity.SmsCode;
import com.homfo.sms.infra.enums.SmsCodeStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Table(name = "USER_SMS_CODES")
@Entity
@NoArgsConstructor
@Getter
public class JpaUserSmsCode extends SmsCode {
    @Id
    private String phoneNumber;

    @Version
    private Long version;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public JpaUserSmsCode(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.count = 0;
        this.status = SmsCodeStatus.REQUESTED;
        this.createdAt = LocalDateTime.now();
    }

    public JpaUserSmsCode(String phoneNumber, String code, LocalDateTime createdAt) {
        this.phoneNumber = phoneNumber;
        this.count = 0;
        this.code = code;
        this.status = SmsCodeStatus.REQUESTED;
        this.createdAt = createdAt;
    }

    @Override
    public void rollback(SmsCodeTransactionDto smsCodeTransactionDto) {
        boolean isValid = Objects.equals(this.phoneNumber, smsCodeTransactionDto.phoneNumber()) &&
                Objects.equals(this.code, smsCodeTransactionDto.after().code()) &&
                Objects.equals(this.status, smsCodeTransactionDto.after().status()) &&
                Objects.equals(this.createdAt, smsCodeTransactionDto.after().createdAt());

        if(isValid) {
            this.code = smsCodeTransactionDto.before().code();
            this.status = smsCodeTransactionDto.before().status();
            this.createdAt = smsCodeTransactionDto.before().createdAt();
            return;
        }

        throw new IllegalArgumentException();
    }
}
