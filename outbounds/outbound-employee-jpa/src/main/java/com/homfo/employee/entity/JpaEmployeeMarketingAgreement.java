package com.homfo.employee.entity;

import com.homfo.employee.infra.attribute_converter.MarketingCodeAttributeConverter;
import com.homfo.enums.MarketingCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "EMPLOYEE_MARKETING_AGREEMENTS")
@Entity
public class JpaEmployeeMarketingAgreement extends EmployeeMarketingAgreement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_marketing_agreement_id")
    private Long id;

    @NotNull
    private Long employeeId;

    @Convert(converter = MarketingCodeAttributeConverter.class)
    @NotNull
    private MarketingCode code;

    private boolean agreement;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder
    public JpaEmployeeMarketingAgreement(Long employeeId, MarketingCode code, boolean agreement) {
        this.employeeId = employeeId;
        this.code = code;
        this.agreement = agreement;
    }
}
