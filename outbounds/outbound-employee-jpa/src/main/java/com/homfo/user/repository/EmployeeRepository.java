package com.homfo.user.repository;

import com.homfo.user.entity.JpaEmployee;
import com.homfo.user.infra.enums.EmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<JpaEmployee, Long> {
    Optional<JpaEmployee> findByAccount(String account);

    Optional<JpaEmployee> findByAccountOrNicknameOrPhoneNumber(String account, String nickname, String phoneNumber);

    Optional<JpaEmployee> findByIdAndStatusNot(Long id, EmployeeStatus status);
}
