package com.homfo.user.entity;

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
}
