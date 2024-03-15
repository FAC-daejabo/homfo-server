package com.homfo.user.repository;

import com.homfo.user.entity.JpaUser;
import com.homfo.user.infra.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<JpaUser, Long> {
    Optional<JpaUser> findByAccount(String account);

    Optional<JpaUser> findByAccountOrNicknameOrPhoneNumber(String account, String nickname, String phoneNumber);

    Optional<JpaUser> findByIdAndStatusNot(Long id, UserStatus status);
}
