package com.homfo.sms.entity;

import com.homfo.error.RequestLimitException;
import com.homfo.sms.command.ValidateSmsCodeCommand;
import com.homfo.sms.dto.SmsCodeTransactionDto;
import com.homfo.sms.infra.enums.SmsCodeStatus;
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
    public static final int CODE_LENGTH = 6;

    public static final int EXPIRED_MINUTES = 5;

    public static final int REQUEST_LIMIT = 5;

    protected String code;

    protected SmsCodeStatus status;

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
     * 인증 코드 상태입니다.
     */
    public SmsCodeStatus getStatus() {
        return status;
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
     * 전화번호랑 코드가 맞는지 확인합니다.
     */
    public boolean verifyCode(ValidateSmsCodeCommand command) {
        if (isExpired() || getCount() > REQUEST_LIMIT) {
            return false;
        }

        boolean isValid = Objects.equals(command.phoneNumber(), getPhoneNumber()) && Objects.equals(command.code(), code);

        if (isValid) {
            status = SmsCodeStatus.SUCCESS;
            return true;
        }

        return false;
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
        boolean limit = !isExpired() && getCount() >= REQUEST_LIMIT;

        if (limit) {
            throw new RequestLimitException(SmsErrorCode.LIMITED_SEND_SMS);
        }

        status = SmsCodeStatus.REQUESTED;
        code = RandomNumberUtil.random(CODE_LENGTH);

        if (isExpired()) {
            count = 1;
            createdAt = LocalDateTime.now();
        } else {
            count++;
        }
    }

    public abstract void rollback(SmsCodeTransactionDto smsCodeTransactionDto);
}
