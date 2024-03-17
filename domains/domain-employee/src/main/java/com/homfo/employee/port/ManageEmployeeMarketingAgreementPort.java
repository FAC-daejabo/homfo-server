package com.homfo.employee.port;

import com.homfo.employee.command.RegisterCommand;
import com.homfo.employee.dto.EmployeeDto;
import com.homfo.employee.dto.EmployeeMarketingAgreementDto;


public interface ManageEmployeeMarketingAgreementPort {
    /**
     * 특정 직원 마케팅 동의 정보를 DB에 저장합니다.
     * */
    EmployeeMarketingAgreementDto save(RegisterCommand command, EmployeeDto employeeDto);
}
