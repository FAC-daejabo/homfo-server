package com.homfo.sms.service;

import com.homfo.sms.command.ValidateSmsCodeCommand;
import com.homfo.sms.dto.SmsCodeDto;
import com.homfo.sms.dto.SmsSendDto;
import com.homfo.sms.port.ManageSmsCodePort;
import com.homfo.sms.port.SendSmsPort;
import com.homfo.sms.usecase.RequestSmsCodeUsecase;
import com.homfo.sms.usecase.ValidateSmsCodeUsecase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidateSmsService implements ValidateSmsCodeUsecase, RequestSmsCodeUsecase {
    private final ManageSmsCodePort manageSmsCodePort;

    private final SendSmsPort sendSmsPort;

    @Autowired
    public ValidateSmsService(ManageSmsCodePort manageSmsCodePort, SendSmsPort sendSmsPort) {
        this.manageSmsCodePort = manageSmsCodePort;
        this.sendSmsPort = sendSmsPort;
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

        sendSmsPort.sendSms(smsSendDto);

        return true;
    }
}
