package com.homfo.user.entity;

import com.homfo.sms.dto.SmsCodeTransactionDto;
import com.homfo.sms.entity.SmsCode;
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
    }

    public void rollback(SmsCodeTransactionDto smsCodeTransactionDto) {
        boolean isValid = Objects.equals(this.phoneNumber, smsCodeTransactionDto.phoneNumber()) &&
                Objects.equals(this.code, smsCodeTransactionDto.after().code()) &&
                Objects.equals(this.status, smsCodeTransactionDto.after().status()) &&
                Objects.equals(this.firstCreatedAt, smsCodeTransactionDto.after().firstCreatedAt()) &&
                Objects.equals(this.createdAt, smsCodeTransactionDto.after().createdAt());

        if(isValid) {
            this.code = smsCodeTransactionDto.before().code();
            this.status = smsCodeTransactionDto.before().status();
            this.firstCreatedAt = smsCodeTransactionDto.before().firstCreatedAt();
            this.createdAt = smsCodeTransactionDto.before().createdAt();
            return;
        }

        throw new IllegalArgumentException();
    }
}
