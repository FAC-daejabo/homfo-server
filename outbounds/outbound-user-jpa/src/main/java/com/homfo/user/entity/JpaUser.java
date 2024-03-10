package com.homfo.user.entity;

import com.homfo.enums.Gender;
import com.homfo.user.infra.attribute_converter.GenderAttributeConverter;
import com.homfo.user.infra.attribute_converter.UserStatusAttributeConverter;
import com.homfo.user.infra.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USERS")
@Entity
public class JpaUser extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(length = 15)
    private String account;

    @Column(length = 60)
    private String password;

    @Column(length = 13)
    private String phoneNumber;

    @Column(length = 15)
    private String nickname;

    @Column(length = 1)
    @Convert(converter = GenderAttributeConverter.class)
    private Gender gender;

    @Column(length = 10)
    private LocalDate birthday;

    @Column(length = 15)
    private String job;

    @Column(length = 1)
    @Convert(converter = UserStatusAttributeConverter.class)
    private UserStatus status;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder
    public JpaUser(String account, String password, String phoneNumber, String nickname, Gender gender, LocalDate birthday, String job) {
        validateAccount(account);
        validateNickname(nickname);
        validatePassword(password);
        validatePhoneNumber(phoneNumber);

        if(job != null) {
            validateJob(job);
        }

        this.account = account;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.nickname = nickname;
        this.gender = gender;
        this.birthday = birthday;
        this.job = job;
        this.status = UserStatus.USE;
    }

    @PrePersist
    public void prePersist() {
        if(status == null) {
            status = UserStatus.USE;
        }
    }

    @Override
    public void signIn(PasswordEncoder encoder, String originPassword) {
        if(!encoder.matches(originPassword, password)) {
            // TODO: exception 변경
            throw new RuntimeException();
        }
    }

    @Override
    public void deleteAccount() {
        account = null;
        password = null;
        phoneNumber = null;
        nickname = null;
        status = UserStatus.DELETED;
    }
}
