package com.hompo.user.repository;

import com.hompo.user.entity.MySqlUser;
import com.hompo.user.infra.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import com.hompo.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<MySqlUser, Long> {
    Optional<MySqlUser> findByAccount(String account);

    Optional<MySqlUser> findByAccountOrNicknameOrPhoneNumber(String account, String nickname, String phoneNumber);

    Optional<MySqlUser> findByIdAndStatusNot(Long id, UserStatus status);
}
