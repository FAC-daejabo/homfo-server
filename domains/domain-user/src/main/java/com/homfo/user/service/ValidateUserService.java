package com.homfo.user.service;

import com.homfo.user.port.ManageUserPort;
import com.homfo.user.usecase.ValidateDuplicateAccountUsecase;
import com.homfo.user.usecase.ValidateDuplicateNicknameUsecase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidateUserService implements ValidateDuplicateAccountUsecase, ValidateDuplicateNicknameUsecase {
    private final ManageUserPort manageUserPort;

    @Autowired
    public ValidateUserService(ManageUserPort manageUserPort) {
        this.manageUserPort = manageUserPort;
    }

    @Override
    public boolean validateAccount(String account) {
        return manageUserPort.existAccount(account);
    }

    @Override
    public boolean validateNickname(String account) {
        return manageUserPort.existNickname(account);
    }
}
