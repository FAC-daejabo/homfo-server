package com.homfo.employee.repository;


import com.homfo.employee.entity.JpaEmployeeRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRefreshTokenRepository extends JpaRepository<JpaEmployeeRefreshToken, Long> {
}
