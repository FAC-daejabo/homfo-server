package com.homfo.employee.dto;

import com.homfo.enums.MarketingCode;

/**
 * 마케팅 동의 여부 DTO 입니다.
 * */
public record MarketingAgreementDto(
        MarketingCode code,

        boolean isAgreement
) {
}
