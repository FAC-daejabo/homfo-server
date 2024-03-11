package com.homfo.employee.entity;

import com.homfo.enums.MarketingCode;

import java.time.LocalDateTime;

/**
 * 직원 마케팅 정보 동의 Entity 입니다.
 * */
public abstract class EmployeeMarketingAgreement {
    /**
     * 직원 마케팅 정보 동의 ID 값입니다.
     * */
    public abstract Long getId();

    /**
     * 직원 ID 값입니다.
     * */
    public abstract Long getEmployeeId();

    /**
     * 마케팅 동의 코드 값입니다.
     * */
    public abstract MarketingCode getCode();

    /**
     * 마케팅 동의 여부 값입니다.
     * */
    public abstract boolean isAgreement();

    /**
     * 생성 시각입니다.
     * */
    public abstract LocalDateTime getCreatedAt();

    /**
     * 수정 시각입니다.
     * */
    public abstract LocalDateTime getUpdatedAt();
}
