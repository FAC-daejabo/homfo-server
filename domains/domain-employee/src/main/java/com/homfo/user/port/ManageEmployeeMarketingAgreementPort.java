package com.homfo.user.port;

import com.homfo.user.command.RegisterCommand;
import com.homfo.user.dto.EmployeeDto;
import com.homfo.user.dto.EmployeeMarketingAgreementDto;


public interface ManageEmployeeMarketingAgreementPort {
    /**
     * 특정 직원 마케팅 동의 정보를 DB에 저장합니다.
     * */
    EmployeeMarketingAgreementDto save(RegisterCommand command, EmployeeDto employeeDto);
}
