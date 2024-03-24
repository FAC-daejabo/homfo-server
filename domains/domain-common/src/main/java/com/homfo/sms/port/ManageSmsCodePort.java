package com.homfo.sms.port;


import com.homfo.sms.command.ValidateSmsCodeCommand;
import com.homfo.sms.dto.SmsCodeDto;
import lombok.NonNull;

/**
 * 문자 인증 코드를 관리합니다.
 */
public interface ManageSmsCodePort {
    /**
     * 문자 인증 코드를 저장합니다. 요청한 횟수도 같이 저장합니다.
     *
     * @throws com.homfo.error.RequestLimitException 가능한 요청 횟수를 넘었을 때
     */
    SmsCodeDto saveSmsCode(@NonNull String phoneNumber);

    /**
     * 전화번호와 인증코드가 맞는지 확인합니다.
     *
     * @throws com.homfo.error.ResourceNotFoundException 전화번호에 맞는 데이터를 찾지 못 했을때
     * */
    boolean verifySmsCode(@NonNull ValidateSmsCodeCommand command);
}
