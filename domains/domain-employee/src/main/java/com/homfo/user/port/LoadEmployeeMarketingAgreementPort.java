package com.homfo.user.port;

import com.homfo.user.dto.EmployeeDto;
import com.homfo.user.dto.EmployeeMarketingAgreementDto;

public interface LoadEmployeeMarketingAgreementPort {
    /**
     * 특정 직원 마케팅 동의 정보를 DB로부터 읽어옵니다.
     * */
    EmployeeMarketingAgreementDto loadMarketingAgreement(EmployeeDto employeeDto);
}
