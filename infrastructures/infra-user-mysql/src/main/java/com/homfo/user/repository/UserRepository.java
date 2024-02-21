package com.homfo.user.repository;

import com.homfo.user.entity.MySqlUser;
import com.homfo.user.infra.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<MySqlUser, Long> {
    Optional<MySqlUser> findByAccount(String account);

    Optional<MySqlUser> findByAccountOrNicknameOrPhoneNumber(String account, String nickname, String phoneNumber);

    Optional<MySqlUser> findByIdAndStatusNot(Long id, UserStatus status);
}
