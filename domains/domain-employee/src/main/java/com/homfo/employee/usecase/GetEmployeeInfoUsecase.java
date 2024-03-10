package com.homfo.employee.usecase;

import com.homfo.employee.dto.EmployeeMarketingAgreementDto;
import com.homfo.error.ResourceNotFoundException;

/**
 * 직원 계정 정보를 가져옵니다.
 * */
public interface GetEmployeeInfoUsecase {
    /**
     * 특정 직원 계정 정보를 가져옵니다.
     *
     * 마케팅 동의 여부도 제공합니다.
     *
     * @throws ResourceNotFoundException 직원 정보가 없다면
     * */
    EmployeeMarketingAgreementDto getEmployeeInfo(long employeeId);
}
