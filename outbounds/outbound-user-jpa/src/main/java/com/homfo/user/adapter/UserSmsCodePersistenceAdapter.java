package com.homfo.user.adapter;

import com.homfo.error.DuplicateRequestException;
import com.homfo.error.ResourceNotFoundException;
import com.homfo.sms.command.ValidateSmsCodeCommand;
import com.homfo.sms.dto.SmsCodeDto;
import com.homfo.sms.dto.SmsCodeTransactionDto;

import com.homfo.sms.infra.enums.SmsErrorCode;
import com.homfo.sms.port.ManageSmsCodePort;

import com.homfo.user.entity.JpaUserSmsCode;
import com.homfo.user.repository.UserSmsCodeRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
public class UserSmsCodePersistenceAdapter implements ManageSmsCodePort {
    private final UserSmsCodeRepository repository;

    @Override
    @Transactional
    public SmsCodeTransactionDto saveSmsCode(@NonNull String phoneNumber) {
        SmsCodeDto before;
        SmsCodeDto after;
        JpaUserSmsCode smsCode = repository.findById(phoneNumber).orElse(new JpaUserSmsCode(phoneNumber));

        before = new SmsCodeDto(smsCode.getPhoneNumber(), smsCode.getCode(), smsCode.getStatus(), smsCode.getCreatedAt());

        try {
            smsCode.createCode();

            after = new SmsCodeDto(smsCode.getPhoneNumber(), smsCode.getCode(), smsCode.getStatus(), smsCode.getCreatedAt());

            repository.save(smsCode);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new DuplicateRequestException(SmsErrorCode.DUPLICATE_REQUEST_SMS);
        }

        return new SmsCodeTransactionDto(smsCode.getPhoneNumber(), before, after);
    }

    @Override
    @Transactional
    public boolean verifySmsCode(@NonNull ValidateSmsCodeCommand command) {
        JpaUserSmsCode smsCode = repository.findByPhoneNumberAndCode(command.phoneNumber(), command.code()).orElseThrow(() -> new ResourceNotFoundException(SmsErrorCode.LIMITED_SEND_SMS));
        boolean isValid = smsCode.verifyCode(command);

        if (isValid) {
            repository.save(smsCode);
            return true;
        }

        return false;
    }

    @Override
    @Transactional
    public SmsCodeDto rollbackSmsCode(@NonNull SmsCodeTransactionDto smsCodeTransactionDto) {
        JpaUserSmsCode smsCode = new JpaUserSmsCode(smsCodeTransactionDto.phoneNumber(), smsCodeTransactionDto.before().code(), smsCodeTransactionDto.before().createdAt());
        repository.save(smsCode);

        return new SmsCodeDto(smsCode.getPhoneNumber(), smsCode.getCode(), smsCode.getStatus(), smsCode.getCreatedAt());
    }
}
