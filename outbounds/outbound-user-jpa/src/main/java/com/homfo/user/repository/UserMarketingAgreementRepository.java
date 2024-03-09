package com.homfo.user.repository;


import com.homfo.user.entity.JpaUserMarketingAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMarketingAgreementRepository extends JpaRepository<JpaUserMarketingAgreement, Long> {
    List<JpaUserMarketingAgreement> findByUserId(Long userId);
}
