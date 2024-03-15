package com.homfo.user.adapter;

import com.homfo.user.command.RegisterCommand;
import com.homfo.user.command.SignInCommand;
import com.homfo.user.dto.EmployeeDto;
import com.homfo.user.entity.JpaEmployee;
import com.homfo.user.infra.enums.EmployeeErrorCode;
import com.homfo.user.infra.enums.EmployeeStatus;
import com.homfo.user.infra.util.ValidationUtil;
import com.homfo.user.port.LoadEmployeePort;
import com.homfo.user.port.ManageEmployeeAccountPort;
import com.homfo.user.repository.EmployeeRepository;
import com.homfo.error.ResourceAlreadyExistException;
import com.homfo.error.ResourceNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EmployeePersistenceAdapter implements LoadEmployeePort, ManageEmployeeAccountPort {
    private final EmployeeRepository employeeRepository;

    private final PasswordEncoder encoder;

    @Override
    public EmployeeDto loadEmployee(long employeeId) {
        JpaEmployee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException(EmployeeErrorCode.NOT_EXIST_EMPLOYEE));
        return new EmployeeDto(employee.getId(), employee.getAccount(), employee.getNickname(), employee.getPhoneNumber(), employee.getGender(), employee.getJob(), employee.getBirthday(), employee.getStatus(), employee.getRole());
    }

    @Override
    public EmployeeDto signIn(@NonNull SignInCommand command) {
        JpaEmployee employee;

        ValidationUtil.validateUserOriginPassword(command.password());

        employee = employeeRepository.findByAccount(command.account()).orElseThrow(() -> new ResourceNotFoundException(EmployeeErrorCode.NOT_EXIST_EMPLOYEE));

        employee.signIn(encoder, command.password());

        return new EmployeeDto(employee.getId(), employee.getAccount(), employee.getNickname(), employee.getPhoneNumber(), employee.getGender(), employee.getJob(), employee.getBirthday(), employee.getStatus(), employee.getRole());
    }

    @Override
    public EmployeeDto register(@NonNull RegisterCommand command) {
        JpaEmployee employee;
        Optional<JpaEmployee> optionalUser;

        ValidationUtil.validateUserOriginPassword(command.password());

        optionalUser = employeeRepository.findByAccountOrNicknameOrPhoneNumber(command.account(), command.nickname(), command.phoneNumber());

        if (optionalUser.isPresent()) {
            throw new ResourceAlreadyExistException(EmployeeErrorCode.ALREADY_EXIST_EMPLOYEE);
        }

        employee = JpaEmployee.builder()
                .account(command.account())
                .password(encoder.encode(command.password()))
                .phoneNumber(command.phoneNumber())
                .nickname(command.nickname())
                .gender(command.gender())
                .birthday(command.birthday())
                .job(command.job())
                .build();

        employeeRepository.save(employee);

        return new EmployeeDto(employee.getId(), employee.getAccount(), employee.getNickname(), employee.getPhoneNumber(), employee.getGender(), employee.getJob(), employee.getBirthday(), employee.getStatus(), employee.getRole());
    }

    @Override
    public void deleteAccount(long employeeId) {
        JpaEmployee employee = employeeRepository.findByIdAndStatusNot(employeeId, EmployeeStatus.DELETED).orElseThrow(() -> new ResourceNotFoundException(EmployeeErrorCode.NOT_EXIST_EMPLOYEE));

        employee.deleteAccount();
        employeeRepository.save(employee);
    }
}
