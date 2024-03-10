package com.homfo.employee.dto;

import java.util.List;

/**
 * 마케팅 동의 여부를 포함한 직원 정보 DTO 입니다.
 * */
public record EmployeeMarketingAgreementDto(
        EmployeeDto employee,

        List<MarketingAgreementDto> marketingAgreementList
) {
}
