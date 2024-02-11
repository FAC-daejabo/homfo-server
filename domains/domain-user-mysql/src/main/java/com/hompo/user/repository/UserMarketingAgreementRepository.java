package com.hompo.user.repository;

import com.hompo.user.entity.MySqlUserMarketingAgreement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMarketingAgreementRepository extends JpaRepository<MySqlUserMarketingAgreement, Long> {
}
