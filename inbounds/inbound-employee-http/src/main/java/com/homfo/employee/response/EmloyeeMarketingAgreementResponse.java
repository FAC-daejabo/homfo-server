package com.homfo.employee.response;


import com.homfo.user.dto.EmployeeMarketingAgreementDto;
import lombok.Getter;

import java.util.List;

/**
 * 마케팅 동의 여부를 포함한 사용자 정보 DTO 입니다.
 * */
@Getter
public class EmloyeeMarketingAgreementResponse {
    private final EmployeeInfoResponse employee;

    private final List<MarketingAgreementResponse> marketingAgreementList;

    public EmloyeeMarketingAgreementResponse(EmployeeMarketingAgreementDto dto) {
        this.employee = new EmployeeInfoResponse(dto.employee());
        this.marketingAgreementList = dto.marketingAgreementList().stream().map(MarketingAgreementResponse::new).toList();
    }
}
