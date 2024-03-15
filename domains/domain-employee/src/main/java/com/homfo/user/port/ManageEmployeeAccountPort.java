package com.homfo.user.port;

import com.homfo.user.command.RegisterCommand;
import com.homfo.user.dto.EmployeeDto;
import lombok.NonNull;

public interface ManageEmployeeAccountPort {
    /**
     * 회원가입합니다.
     *
     * @throws com.homfo.error.ResourceAlreadyExistException 이미 존재하는 직원 정보라면
     */
    EmployeeDto register(@NonNull RegisterCommand command);

    /**
     * 회원탈퇴합니다.
     *
     * @throws com.homfo.error.ResourceNotFoundException 존재하지 않는 직원이라면
     */
    void deleteAccount(long employeeId);
}
