package com.homfo.sms.adapter;

import com.homfo.error.DuplicateRequestException;
import com.homfo.error.ResourceNotFoundException;
import com.homfo.sms.command.ValidateSmsCodeCommand;
import com.homfo.sms.dto.SmsCodeDto;
import com.homfo.sms.entity.JpaSmsCode;
import com.homfo.sms.infra.enums.SmsErrorCode;
import com.homfo.sms.port.ManageSmsCodePort;
import com.homfo.sms.repository.SmsCodeRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
public class SmsCodePersistenceAdapter implements ManageSmsCodePort {
    private final SmsCodeRepository repository;

    @Override
    @Transactional
    public SmsCodeDto saveSmsCode(@NonNull String phoneNumber) {
        JpaSmsCode smsCode = repository.findById(phoneNumber).orElse(new JpaSmsCode(phoneNumber));

        try {
            smsCode.createCode();
            repository.save(smsCode);
        } catch(ObjectOptimisticLockingFailureException e) {
            throw new DuplicateRequestException(SmsErrorCode.DUPLICATE_REQUEST_SMS);
        }

        return new SmsCodeDto(smsCode.getPhoneNumber(), smsCode.getCode());
    }

    @Override
    public boolean verifySmsCode(@NonNull ValidateSmsCodeCommand command) {
        JpaSmsCode smsCode = repository.findByPhoneNumberAndCode(command.phoneNumber(), command.code()).orElseThrow(() -> new ResourceNotFoundException(SmsErrorCode.LIMITED_SEND_SMS));

        return smsCode.verifyCode(command);
    }
}
