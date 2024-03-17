package com.homfo.employee.port;

import com.homfo.employee.command.SignInCommand;
import com.homfo.employee.dto.EmployeeDto;
import com.homfo.error.ResourceNotFoundException;
import lombok.NonNull;

public interface LoadEmployeePort {
    /**
     * 특정 직원 정보를 읽어옵니다.
     *
     * @throws ResourceNotFoundException 직원 정보가 없다면
     * */
    EmployeeDto loadEmployee(long employeeId);

    /**
     * 특정 직원에 대하여 로그인을 시도합니다.
     *
     * @throws ResourceNotFoundException 직원 정보가 없다면
     * */
    EmployeeDto signIn(@NonNull SignInCommand command);
}
