package com.homfo.user.entity;

import com.homfo.user.infra.enums.EmployeeRole;
import com.homfo.user.infra.enums.EmployeeStatus;
import com.homfo.enums.Gender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MockEmployee extends Employee {
    private Long id;

    private String account;

    private String password;

    private String phoneNumber;

    private String nickname;

    private Gender gender;

    private LocalDate birthday;

    private String job;

    private EmployeeStatus status;

    private EmployeeRole role;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Override
    public void signIn(PasswordEncoder encoder, String originPassword) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAccount() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getAccount() {
        return account;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public Gender getGender() {
        return gender;
    }

    @Override
    public LocalDate getBirthday() {
        return birthday;
    }

    @Override
    public String getJob() {
        return job;
    }

    @Override
    public EmployeeStatus getStatus() {
        return status;
    }

    @Override
    public EmployeeRole getRole() {
        return role;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
