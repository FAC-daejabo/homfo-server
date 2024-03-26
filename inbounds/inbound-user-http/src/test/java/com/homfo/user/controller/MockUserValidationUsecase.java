package com.homfo.user.controller;

import com.homfo.sms.command.ValidateSmsCodeCommand;
import com.homfo.sms.usecase.RequestSmsCodeUsecase;
import com.homfo.sms.usecase.ValidateSmsCodeUsecase;
import com.homfo.user.usecase.ValidateDuplicateAccountUsecase;
import com.homfo.user.usecase.ValidateDuplicateNicknameUsecase;

class MockUserValidationUsecase implements
        ValidateDuplicateAccountUsecase,
        ValidateDuplicateNicknameUsecase,
        ValidateSmsCodeUsecase,
        RequestSmsCodeUsecase
{


    @Override
    public boolean requestSmsCode(String phoneNumber) {
        return false;
    }

    @Override
    public boolean validateSmsCode(ValidateSmsCodeCommand command) {
        return false;
    }

    @Override
    public boolean validateAccount(String account) {
        return false;
    }

    @Override
    public boolean validateNickname(String nickname) {
        return false;
    }
}