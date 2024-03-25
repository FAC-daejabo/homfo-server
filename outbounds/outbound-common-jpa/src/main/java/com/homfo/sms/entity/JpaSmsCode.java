package com.homfo.sms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Table(name = "SMS_CODES")
@Entity
@NoArgsConstructor
@Getter
public class JpaSmsCode extends SmsCode {
    @Id
    private String phoneNumber;

    @Version
    private Long version;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public JpaSmsCode(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.createdAt = LocalDateTime.now();
    }

    public JpaSmsCode(String phoneNumber, String code, LocalDateTime createdAt) {
        this.phoneNumber = phoneNumber;
        this.code = code;
        this.createdAt = createdAt;
    }
}
