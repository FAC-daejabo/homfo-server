package com.homfo.user.service;

import com.homfo.user.command.ValidateSmsCodeCommand;
import com.homfo.user.dto.SmsCodeDto;
import com.homfo.user.dto.SmsSendDto;
import com.homfo.user.port.*;
import com.homfo.user.usecase.RequestSmsCodeUsecase;
import com.homfo.user.usecase.ValidateDuplicateAccountUsecase;
import com.homfo.user.usecase.ValidateDuplicateNicknameUsecase;
import com.homfo.user.usecase.ValidateSmsCodeUsecase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidateUserService implements ValidateDuplicateAccountUsecase, ValidateDuplicateNicknameUsecase, ValidateSmsCodeUsecase, RequestSmsCodeUsecase {
    private final ManageUserPort manageUserPort;

    private final ManageSmsCodePort manageSmsCodePort;

    private final SendSmsPort sendSmsPort;

    @Autowired
    public ValidateUserService(ManageUserPort manageUserPort, ManageSmsCodePort manageSmsCodePort, SendSmsPort sendSmsPort) {
        this.manageUserPort = manageUserPort;
        this.manageSmsCodePort = manageSmsCodePort;
        this.sendSmsPort = sendSmsPort;
    }

    @Override
    public boolean validateAccount(String account) {
        return manageUserPort.existAccount(account);
    }

    @Override
    public boolean validateNickname(String account) {
        return manageUserPort.existNickname(account);
    }

    @Override
    public boolean validateSmsCode(ValidateSmsCodeCommand command) {
        return manageSmsCodePort.verifySmsCode(command);
    }

    @Override
    public boolean requestSmsCode(String phoneNumber) {
        SmsCodeDto smsCodeDto = manageSmsCodePort.saveSmsCode(phoneNumber);
        String message = "[홈포] 인증번호: " + smsCodeDto.code() + "\n타인 유출로 인한 피해 주의";
        SmsSendDto smsSendDto = new SmsSendDto(smsCodeDto.phoneNumber(), message);

        sendSmsPort.send(smsSendDto);

        return true;
    }
}
