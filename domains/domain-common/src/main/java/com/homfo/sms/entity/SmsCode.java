package com.homfo.sms.entity;

import com.homfo.error.RequestLimitException;
import com.homfo.sms.command.ValidateSmsCodeCommand;
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
    /**
     * 인증 코드 길이입니다.
     */
    public static final int CODE_LENGTH = 6;

    /**
     * 인증 코드가 만료되는 시각입니다.
     */
    public static final int EXPIRED_MINUTES = 5;

    /**
     * 인증 코드를 최대로 요청할 수 있는 횟수입니다.
     */
    public static final int REQUEST_LIMIT = 5;

    /**
     * {@value CODE_LENGTH} 길이에 해당되는 10진수 숫자로 이루어진 문자열입니다.
     */
    protected String code;

    /**
     * 인증 코드의 상태입니다.
     */
    protected SmsCodeStatus status;

    /**
     * 인증 코드가 생성된 횟수를 저장합니다.
     */
    protected Integer count;

    /**
     * {@value EXPIRED_MINUTES} 분 내로 첫번째 요청됐던 시각을 저장합니다.
     */
    protected LocalDateTime firstCreatedAt;

    /**
     * 인증 코드가 생성된 시각을 저장합니다.
     */
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
     * {@value EXPIRED_MINUTES}분 내로 첫번째 문자 메세지를 보낸 시각입니다.
     */
    public LocalDateTime getFirstCreatedAt() {
        return firstCreatedAt;
    }

    /**
     * 문자 메세지를 보낸 시각입니다.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * 데이터 수정 시각입니다.
     */
    public abstract LocalDateTime getUpdatedAt();


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
     * {@value EXPIRED_MINUTES}분 내에 {@value REQUEST_LIMIT}번을 더이상 문자를 보낼 수 없습니다.
     * {@value EXPIRED_MINUTES}분이 지난 뒤에 문자를 보내면 1회로 초기화 됩니다.
     *
     * @throws RequestLimitException
     */
    public void createCode() {
        if (createLimited()) {
            throw new RequestLimitException(SmsErrorCode.LIMITED_REQUEST_SMS);
        }

        Integer count = getCount();
        boolean notCreated = count == null || count == 0;

        status = SmsCodeStatus.REQUESTED;
        code = RandomNumberUtil.random(CODE_LENGTH);
        createdAt = LocalDateTime.now();

        if (notCreated || isCreateTimeLimitExpired()) {
            this.count = 1;
            firstCreatedAt = createdAt;
        } else {
            this.count++;
        }
    }

    /**
     * {@value EXPIRED_MINUTES}분 내 {@value REQUEST_LIMIT}회 생성 제한 정책이 만료되었는지 확인합니다.
     */
    public boolean isCreateTimeLimitExpired() {
        long minutesSinceFirstCreate = Duration.between(getFirstCreatedAt(), LocalDateTime.now()).toMinutes();

        return minutesSinceFirstCreate >= EXPIRED_MINUTES;
    }

    /**
     * 인증 코드가 {@value EXPIRED_MINUTES}분 내로 요청된 것인지 확인합니다.
     */
    public boolean createLimited() {
        LocalDateTime createdAt = getCreatedAt();

        if(createdAt == null) {
            return false;
        }

        long minutesSinceCreate = Duration.between(createdAt, LocalDateTime.now()).toMinutes();

        if(getCount() >= REQUEST_LIMIT) {
            if(isCreateTimeLimitExpired()) {
                return minutesSinceCreate < 1;
            }
            return true;
        }
        return minutesSinceCreate < 1;
    }
}
