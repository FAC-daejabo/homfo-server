package com.homfo.user.entity;

import com.homfo.user.infra.attribute_converter.GenderAttributeConverter;
import com.homfo.user.infra.attribute_converter.EmployeeStatusAttributeConverter;
import com.homfo.user.infra.enums.EmployeeRole;
import com.homfo.user.infra.enums.EmployeeStatus;
import com.homfo.enums.Gender;

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
@Table(name = "EMPLOYEES")
@Entity
public class JpaEmployee extends Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
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
    @Convert(converter = EmployeeStatusAttributeConverter.class)
    private EmployeeStatus status;

    private EmployeeRole role;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder
    public JpaEmployee(String account, String password, String phoneNumber, String nickname, Gender gender, LocalDate birthday, String job) {
        validateAccount(account);
        validateNickname(nickname);
        validatePassword(password);
        validatePhoneNumber(phoneNumber);

        if (job != null) {
            validateJob(job);
        }

        this.account = account;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.nickname = nickname;
        this.gender = gender;
        this.birthday = birthday;
        this.job = job;
        this.status = EmployeeStatus.PENDING;
        this.role = EmployeeRole.EMPLOYEE;
    }

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = EmployeeStatus.PENDING;
        }
    }

    @Override
    public void signIn(PasswordEncoder encoder, String originPassword) {
        if (!encoder.matches(originPassword, password)) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void deleteAccount() {
        account = null;
        password = null;
        phoneNumber = null;
        nickname = null;
        status = EmployeeStatus.DELETED;
        role = null;
    }
}
