package com.hompo.user.repository;

import com.hompo.user.entity.MySqlUserMarketingAgreement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserMarketingAgreementRepository extends JpaRepository<MySqlUserMarketingAgreement, Long> {
    List<MySqlUserMarketingAgreement> findByUserId(Long userId);
}
