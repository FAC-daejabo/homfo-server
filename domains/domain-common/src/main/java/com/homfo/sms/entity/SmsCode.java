package com.homfo.sms.entity;

import com.homfo.error.RequestLimitException;
import com.homfo.sms.command.ValidateSmsCodeCommand;
import com.homfo.sms.infra.enums.SmsErrorCode;
import com.homfo.util.RandomNumberUtil;
import jakarta.persistence.MappedSuperclass;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Sms 코드 인증을 담당하는 Entity 입니다.
 */
@MappedSuperclass
public abstract class SmsCode {
    private static final int CODE_LENGTH = 6;

    private static final int EXPIRED_MINUTES = 5;

    private static final int REQUEST_LIMIT = 5;

    protected String code;

    protected Integer count;

    protected LocalDateTime createdAt;

    /**
     * Sms 코드를 보낸 전화번호입니다. 유일한 값이어야 합니다.
     */
    public abstract String getPhoneNumber();

    /**
     * Sms 인증 코드입니다.
     */
    public String getCode() {
        return code;
    }

    /**
     * Entity 전화번호에 Sms 코드를 보낸 횟수입니다.
     */
    public Integer getCount() {
        return count;
    }

    /**
     * 5분 내로 첫번째 문자 메세지를 보낸 시각입니다.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * 데이터 수정 시각입니다.
     */
    public abstract LocalDateTime getUpdatedAt();

    /**
     * 낙관적 락을 위해 사용합니다.
     */
    public abstract Long getVersion();


    /**
     * 인증 코드가 만료되었는지 확인합니다.
     */
    public boolean isExpired() {
        long minutesSinceLastUpdate = Duration.between(getCreatedAt(), LocalDateTime.now()).toMinutes();

        return minutesSinceLastUpdate >= EXPIRED_MINUTES;
    }

    /**
     * 이미 제한된 인증 코드인지 확인합니다.
     */
    public boolean isLimited() {
        return getCount() >= REQUEST_LIMIT;
    }

    /**
     * 전화번호랑 코드가 맞는지 확인합니다.
     */
    public boolean verifyCode(ValidateSmsCodeCommand command) {
        if (isExpired() || isLimited()) {
            return false;
        }

        return Objects.equals(command.phoneNumber(), getPhoneNumber()) && Objects.equals(command.code(), code);
    }

    /**
     * Entity 전화번호에 Sms 코드를 보냅니다.
     * <p>
     * 5분 내에 5번을 더이상 문자를 보낼 수 없습니다.
     * 5분이 지난 뒤에 문자를 보내면 1회로 초기화 됩니다.
     *
     * @throws RequestLimitException
     */
    public void createCode() {
        if (isLimited()) {
            throw new RequestLimitException(SmsErrorCode.LIMITED_SEND_SMS);
        }

        if (isExpired()) {
            count = 1;
            createdAt = LocalDateTime.now();
        } else {
            count++;
        }

        generateCode();
    }

    /**
     * 인증 코드를 생성합니다.
     */
    private void generateCode() {
        this.code = RandomNumberUtil.random(CODE_LENGTH);
    }
}
