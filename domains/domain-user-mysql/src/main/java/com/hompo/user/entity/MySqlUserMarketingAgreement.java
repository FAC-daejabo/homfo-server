package com.hompo.user.entity;

import com.hompo.enums.MarketingCode;
import com.hompo.user.infra.attributeConverter.MarketingCodeAttributeConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USER_MARKETING_AGREEMENT")
@Entity
public class MySqlUserMarketingAgreement extends UserMarketingAgreement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long userId;

    @Convert(converter = MarketingCodeAttributeConverter.class)
    @NotNull
    private MarketingCode code;

    private boolean agreement;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
