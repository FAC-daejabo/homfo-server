package com.homfo.user.repository;


import com.homfo.user.entity.JpaEmployeeRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRefreshTokenRepository extends JpaRepository<JpaEmployeeRefreshToken, Long> {
}
