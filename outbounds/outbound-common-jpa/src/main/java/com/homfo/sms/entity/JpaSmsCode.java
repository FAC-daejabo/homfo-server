package com.homfo.sms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Table(name = "SMS_CODES")
@Entity
@NoArgsConstructor
@Getter
public class JpaSmsCode extends SmsCode {
    @Id
    private String phoneNumber;

    @CreationTimestamp
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    public JpaSmsCode(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.updatedAt = LocalDateTime.now();
    }
}
