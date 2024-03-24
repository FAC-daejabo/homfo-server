package com.homfo.sms.entity;

import com.homfo.error.RequestLimitException;
import com.homfo.sms.command.ValidateSmsCodeCommand;
import com.homfo.sms.infra.enums.SmsErrorCode;
import jakarta.persistence.MappedSuperclass;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Sms 코드 인증을 담당하는 Entity 입니다.
 */
@MappedSuperclass
public abstract class SmsCode {
    protected Integer count;

    /**
     * Sms 코드를 보낸 전화번호입니다. 유일한 값이어야 합니다.
     */
    public abstract String getPhoneNumber();

    /**
     * Sms 인증 코드입니다.
     */
    public abstract String getCode();

    /**
     * Entity 전화번호에 Sms 코드를 보낸 횟수입니다.
     */
    public Integer getCount() {
        return count;
    }

    public abstract LocalDateTime getUpdatedAt();

    /**
     * 전화번호랑 코드가 맞는지 확인합니다.
     */
    public boolean verifyCode(ValidateSmsCodeCommand command) {
        return Objects.equals(command.phoneNumber(), getPhoneNumber()) && Objects.equals(command.code(), getCode());
    }

    /**
     * Entity 전화번호에 Sms 코드를 보냅니다.
     * <p>
     * 5분 내에 5번을 더이상 문자를 보낼 수 없습니다.
     * 5분이 지난 뒤에 문자를 보내면 1회로 초기화 됩니다.
     *
     * @throws RequestLimitException
     */
    public void sendSmsCode() {
        if (getCount() >= 5) {
            throw new RequestLimitException(SmsErrorCode.LIMITED_SEND_SMS);
        }

        // 5분 이상 차이나는지 확인
        long minutesSinceLastUpdate = Duration.between(getUpdatedAt(), LocalDateTime.now()).toMinutes();

        if (minutesSinceLastUpdate < 5) {
            count++;
        } else {
            count = 1;
        }
    }
}
