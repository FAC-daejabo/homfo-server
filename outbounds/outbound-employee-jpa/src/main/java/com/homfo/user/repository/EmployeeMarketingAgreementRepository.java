package com.homfo.user.repository;


import com.homfo.user.entity.JpaEmployeeMarketingAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeMarketingAgreementRepository extends JpaRepository<JpaEmployeeMarketingAgreement, Long> {
    List<JpaEmployeeMarketingAgreement> findByEmployeeId(Long employeeId);
}
