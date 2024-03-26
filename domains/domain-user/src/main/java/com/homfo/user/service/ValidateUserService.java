package com.homfo.user.service;

import com.homfo.error.BadRequestException;
import com.homfo.error.CommonErrorCode;
import com.homfo.error.ThirdPartyUnavailableException;
import com.homfo.sms.command.ValidateSmsCodeCommand;
import com.homfo.sms.dto.SmsCodeTransactionDto;
import com.homfo.sms.dto.SmsSendDto;
import com.homfo.sms.port.ManageSmsCodePort;
import com.homfo.sms.port.SendSmsPort;
import com.homfo.sms.usecase.RequestSmsCodeUsecase;
import com.homfo.sms.usecase.ValidateSmsCodeUsecase;
import com.homfo.user.entity.User;
import com.homfo.user.port.ManageUserPort;
import com.homfo.user.usecase.ValidateDuplicateAccountUsecase;
import com.homfo.user.usecase.ValidateDuplicateNicknameUsecase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@Slf4j
public class ValidateUserService implements ValidateDuplicateAccountUsecase, ValidateDuplicateNicknameUsecase, ValidateSmsCodeUsecase, RequestSmsCodeUsecase {
    private final ManageUserPort manageUserPort;

    private final ManageSmsCodePort manageSmsCodePort;

    private final SendSmsPort sendSmsPort;

    @Autowired
    public ValidateUserService(
            ManageUserPort manageUserPort,
            @Qualifier("userSmsCodePersistenceAdapter") ManageSmsCodePort manageSmsCodePort,
            SendSmsPort sendSmsPort
    ) {
        this.manageUserPort = manageUserPort;
        this.manageSmsCodePort = manageSmsCodePort;
        this.sendSmsPort = sendSmsPort;
    }

    @Override
    public boolean validateAccount(String account) {
        Pattern pattern = Pattern.compile(User.ACCOUNT_REGEXP);

        if (!pattern.matcher(account).matches()) {
            throw new BadRequestException(CommonErrorCode.BAD_REQUEST);
        }

        return manageUserPort.existAccount(account);
    }

    @Override
    public boolean validateNickname(String nickname) {
        Pattern pattern = Pattern.compile(User.NICKNAME_REGEXP);

        if (!pattern.matcher(nickname).matches()) {
            throw new BadRequestException(CommonErrorCode.BAD_REQUEST);
        }

        return manageUserPort.existNickname(nickname);
    }

    @Override
    public boolean validateSmsCode(ValidateSmsCodeCommand command) {
        return manageSmsCodePort.verifySmsCode(command);
    }

    @Override
    public boolean requestSmsCode(String phoneNumber) {
        Pattern pattern = Pattern.compile(User.PHONE_NUMBER_REGEXP);

        if (!pattern.matcher(phoneNumber).matches()) {
            throw new BadRequestException(CommonErrorCode.BAD_REQUEST);
        }

        SmsCodeTransactionDto smsCodeTransactionDto = manageSmsCodePort.createSmsCode(phoneNumber);
        String message = "[홈포] 인증번호: " + smsCodeTransactionDto.after().code() + "\n타인 유출로 인한 피해 주의";
        SmsSendDto smsSendDto = new SmsSendDto(smsCodeTransactionDto.phoneNumber().replaceAll("-", ""), message);

        try {
            sendSmsPort.sendSms(smsSendDto);
            return true;
        } catch (ThirdPartyUnavailableException e) {
            log.warn("SendSmsPort error " + e);
            manageSmsCodePort.rollbackSmsCode(smsCodeTransactionDto);
            throw e;
        }
    }
}
