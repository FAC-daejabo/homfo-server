package com.homfo.user.repository;

import com.homfo.user.entity.MySqlRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<MySqlRefreshToken, Long> {
}
