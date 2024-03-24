package com.homfo.sms.port;

import com.homfo.sms.dto.SmsSendDto;
import lombok.NonNull;

/**
 * SMS를 담당하는 외부 서비스에 문자를 보내달라고 요청합니다.
 */
public interface SendSmsPort {
    /**
     * 문자를 보내달라고 요청합니다.
     *
     * @throws com.homfo.error.ThirdPartyUnavailableException 외부 서비스에서 장애가 발생했을 때
     */
    void sendSms(@NonNull SmsSendDto smsSendDto);
}
