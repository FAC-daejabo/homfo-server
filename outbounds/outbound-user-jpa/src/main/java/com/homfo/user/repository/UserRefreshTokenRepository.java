package com.homfo.user.repository;


import com.homfo.user.entity.JpaUserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRefreshTokenRepository extends JpaRepository<JpaUserRefreshToken, Long> {
}
