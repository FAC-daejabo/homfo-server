package com.homfo.employee.service;

import com.homfo.auth.command.TokenRefreshCommand;
import com.homfo.auth.dto.JwtDto;
import com.homfo.auth.dto.JwtSecretDto;
import com.homfo.auth.infra.util.JwtUtil;
import com.homfo.auth.port.LoadJwtPort;
import com.homfo.auth.port.ManageJwtPort;
import com.homfo.employee.command.RegisterCommand;
import com.homfo.employee.command.SignInCommand;
import com.homfo.employee.dto.EmployeeDto;
import com.homfo.employee.dto.EmployeeMarketingAgreementDto;
import com.homfo.employee.port.LoadEmployeeMarketingAgreementPort;
import com.homfo.employee.port.LoadEmployeePort;
import com.homfo.employee.port.ManageEmployeeAccountPort;
import com.homfo.employee.port.ManageEmployeeMarketingAgreementPort;
import com.homfo.employee.usecase.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ManageEmployeeService implements GetEmployeeInfoUsecase, SignInUsecase, SignOutUsecase, RegisterUsecase, DeleteAccountUsecase, TokenRefreshUsecase {
    private final LoadEmployeePort loadEmployeePort;

    private final LoadJwtPort loadJwtPort;

    private final LoadEmployeeMarketingAgreementPort loadUserMarketingAgreementPort;

    private final ManageEmployeeAccountPort manageEmployeeAccountPort;

    private final ManageJwtPort manageJwtPort;

    private final ManageEmployeeMarketingAgreementPort manageUserMarketingAgreementPort;

    private final JwtSecretDto employeeAccessTokenInfo;

    private final JwtSecretDto employeeRefreshTokenInfo;

    public ManageEmployeeService(
            LoadEmployeePort loadEmployeePort,
            @Qualifier("employeeRefreshTokenPersistenceAdapter") LoadJwtPort loadJwtPort,
            LoadEmployeeMarketingAgreementPort loadUserMarketingAgreementPort,
            ManageEmployeeAccountPort manageEmployeeAccountPort,
            @Qualifier("employeeRefreshTokenPersistenceAdapter") ManageJwtPort manageJwtPort,
            ManageEmployeeMarketingAgreementPort manageUserMarketingAgreementPort,
            JwtSecretDto employeeAccessTokenInfo,
            JwtSecretDto employeeRefreshTokenInfo
    ) {
        this.loadEmployeePort = loadEmployeePort;
        this.loadJwtPort = loadJwtPort;
        this.loadUserMarketingAgreementPort = loadUserMarketingAgreementPort;
        this.manageEmployeeAccountPort = manageEmployeeAccountPort;
        this.manageJwtPort = manageJwtPort;
        this.manageUserMarketingAgreementPort = manageUserMarketingAgreementPort;
        this.employeeAccessTokenInfo = employeeAccessTokenInfo;
        this.employeeRefreshTokenInfo = employeeRefreshTokenInfo;
    }

    @Override
    public EmployeeMarketingAgreementDto getEmployeeInfo(long employeeId) {
        EmployeeDto employeeDto = loadEmployeePort.loadEmployee(employeeId);
        return loadUserMarketingAgreementPort.loadMarketingAgreement(employeeDto);
    }

    @Override
    @Transactional
    public void deleteAccount(long employeeId) {
        manageEmployeeAccountPort.deleteAccount(employeeId);
        manageJwtPort.deleteToken(employeeId);
    }

    @Override
    @Transactional
    public JwtDto register(RegisterCommand command) {
        EmployeeDto employeeDto = manageEmployeeAccountPort.register(command);
        String accessToken = JwtUtil.createToken(employeeDto.id(), employeeAccessTokenInfo);
        String refreshToken = manageJwtPort.save(employeeDto.id(), employeeRefreshTokenInfo);

        manageUserMarketingAgreementPort.save(command, employeeDto);

        return new JwtDto(accessToken, refreshToken);
    }

    @Override
    public JwtDto signIn(SignInCommand command) {
        EmployeeDto employeeDto = loadEmployeePort.signIn(command);
        String accessToken = JwtUtil.createToken(employeeDto.id(), employeeAccessTokenInfo);
        String refreshToken = manageJwtPort.save(employeeDto.id(), employeeRefreshTokenInfo);

        return new JwtDto(accessToken, refreshToken);
    }

    @Override
    public JwtDto refreshToken(long employeeId, TokenRefreshCommand command) {
        String refreshToken = loadJwtPort.getVerifyToken(command.token(), employeeRefreshTokenInfo);
        String accessToken = JwtUtil.createToken(employeeId, employeeAccessTokenInfo);

        return new JwtDto(accessToken, refreshToken);
    }

    @Override
    public void signOut(long employeeId) {
        manageJwtPort.deleteToken(employeeId);
    }
}
