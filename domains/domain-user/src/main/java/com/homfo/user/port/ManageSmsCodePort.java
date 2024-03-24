package com.homfo.user.port;

import com.homfo.user.dto.SmsCodeDto;
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
}
