package com.hompo.user.repository;

import com.hompo.user.entity.MySqlRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<MySqlRefreshToken, Long> {
}
