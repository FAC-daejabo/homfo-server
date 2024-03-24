package com.homfo.user.port;

import com.homfo.user.dto.SmsCodeDto;
import lombok.NonNull;

/**
 * 문자 인증 코드 정보를 불러옵니다.
 */
public interface LoadSmsCodePort {
    /**
     * 문자 인증 코드 정보를 불러옵니다.
     *
     * @throws com.homfo.error.ResourceNotFoundException 전화번호에 해당하는 SmsCode가 없을 때
     */
    SmsCodeDto loadSmsCode(@NonNull String phoneNumber);
}
