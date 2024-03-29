package com.homfo.employee.repository;


import com.homfo.employee.entity.JpaEmployeeMarketingAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeMarketingAgreementRepository extends JpaRepository<JpaEmployeeMarketingAgreement, Long> {
    List<JpaEmployeeMarketingAgreement> findByEmployeeId(Long employeeId);
}
