package com.homfo.employee.repository;

import com.homfo.employee.entity.JpaEmployee;
import com.homfo.employee.infra.enums.EmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<JpaEmployee, Long> {
    Optional<JpaEmployee> findByAccount(String account);

    Optional<JpaEmployee> findByAccountOrNicknameOrPhoneNumber(String account, String nickname, String phoneNumber);

    Optional<JpaEmployee> findByIdAndStatusNot(Long id, EmployeeStatus status);
}
