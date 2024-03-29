package com.homfo.sms.usecase;


import com.homfo.sms.command.ValidateSmsCodeCommand;

/**
 * 사용자 전화번호가 중복되는지, 올바른 전화번호인지 확인합니다.
 * */
public interface ValidateSmsCodeUsecase {
    /**
     * 사용자 전화번호가 중복되는지, 올바른 전화번호인지 확인합니다.
     *
     * @throws com.homfo.error.ResourceNotFoundException 존재하지 않는 전화번호라면
     * @throws com.homfo.error.ThirdPartyUnavailableException 외부 인증 서비스에서 장애가 발생한 경우
     * */
    boolean validateSmsCode(ValidateSmsCodeCommand command);
}