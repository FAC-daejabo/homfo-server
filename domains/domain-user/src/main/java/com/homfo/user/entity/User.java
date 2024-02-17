package com.homfo.user.entity;

import com.homfo.auth.dto.JwtDto;
import com.homfo.enums.Gender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;
import com.homfo.user.infra.enums.UserStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 사용자 Entity 입니다.
 * */
public abstract class User {
    /**
     * 계정은 8자 이상, 15자 이하의 대소문자 및 숫자로 구성되어야 합니다.
     */
    protected final String ACCOUNT_REGEXP = "^[a-zA-Z\\d]{8,15}$";

    protected final String ENCRYPTED_PASSWORD_REGEXP = "^\\$2[ayb]\\$\\d{2}\\$[./A-Za-z0-9]+$";

    /**
     * 닉네임은 1자 이상, 15자 이하의 대소문자, 숫자, 한글로 구성되어야 합니다.
     */
    protected final String NICKNAME_REGEXP = "^[a-zA-Z\\d가-힣]{1,15}$";

    /**
     * 전화번호는 xxx-xxxx-xxxx 형태여야 합니다.
     */
    protected final String PHONE_NUMBER_REGEXP = "^\\d{3}-\\d{4}-\\d{4}$";

    /**
     * 직업은 1자 이상, 15자 이하의 대소문자, 한글로 구성되어야 합니다.
     */
    protected final String JOB_REGEXP = "^(?=.*[가-힣a-zA-Z])[가-힣a-zA-Z]{1,15}$";

    /**
     * PK 입니다.
     */
    public abstract Long getId();

    /**
     * 계정입니다. 8자 이상, 15자 이하의 대소문자 및 숫자로 구성되어 있습니다.
     * <p>
     * Unique 값이어야합니다.
     */
    public abstract String getAccount();

    /**
     * 비밀번호입니다. 8자 이상, 15자 이하의 대소문자, 숫자, 특수문자로 구성되어 있습니다.
     */
    public abstract String getPassword();

    /**
     * 전화번호입니다. xxx-xxxx-xxxx 형태여야 합니다.
     * <p>
     * Unique 값이어야합니다.
     */
    public abstract String getPhoneNumber();

    /**
     * 닉네임입니다. 1자 이상, 15자 이하의 대소문자, 숫자, 한글로 구성되어 있습니다.
     * <p>
     * Unique 값이어야합니다.
     * Nullable 입니다.
     */
    public abstract String getNickname();

    /**
     * 성별입니다. Nullable 입니다.
     */
    public abstract Gender getGender();

    /**
     * 생일입니다. yyyy-MM-dd로 구성되어 있습니다.
     * <p>
     * Nullable 입니다.
     */
    public abstract LocalDate getBirthday();

    /**
     * 직업입니다. 1자 이상, 15자 이하의 대소문자, 한글로 구성되어 있습니다.
     * <p>
     * Nullable 입니다.
     */
    public abstract String getJob();

    /**
     * 사용자의 계정 상태입니다.
     */
    public abstract UserStatus getStatus();

    /**
     * 사용자 정보 생성 시점입니다.
     */
    public abstract LocalDateTime getCreatedAt();

    /**
     * 사용자 정보 변경 시점입니다.
     */
    public abstract LocalDateTime getUpdatedAt();

    /**
     * 암호화된 비밀번호와 일치하는지 확인합니다.
     *
     * 일치하지 않으면 [RuntimeException]이 발생합니다.
     * */
    public abstract void signIn(PasswordEncoder encoder, String originPassword);

    /**
     * 계정 정보를 삭제합니다.
     *
     * 사용자를 특정할 수 없도록 정보를 모두 null로 업데이트 하고, status는 삭제된 사용자로 변경합니다.
     * */
    public abstract void deleteAccount();

    /**
     * 계정 문자열이 올바른지 확인합니다.
     *
     * 올바르지 않다면 [IllegalArgumentException]이 발생합니다.
     * */
    protected void validateAccount(String account) {
        Assert.isTrue(Pattern.matches(ACCOUNT_REGEXP, Objects.requireNonNull(account)), "올바르지 않은 계정입니다.");
    }

    /**
     * 닉네임 문자열이 올바른지 확인합니다.
     *
     * 올바르지 않다면 [IllegalArgumentException]이 발생합니다.
     * */
    protected void validateNickname(String nickname) {
        Assert.isTrue(Pattern.matches(NICKNAME_REGEXP, Objects.requireNonNull(nickname)), "올바르지 않은 닉네임입니다.");
    }

    /**
     * 비밀번호 문자열이 올바른지 확인합니다.
     *
     * 올바르지 않다면 [IllegalArgumentException]이 발생합니다.
     * */
    protected void validatePassword(String password) {
        Assert.isTrue(Pattern.matches(ENCRYPTED_PASSWORD_REGEXP, Objects.requireNonNull(password)), "올바르지 않은 비밀번호입니다.");
    }

    protected void validatePhoneNumber(String phoneNumber) {
        Assert.isTrue(Pattern.matches(PHONE_NUMBER_REGEXP, Objects.requireNonNull(phoneNumber)), "올바르지 않은 전화번호입니다.");
    }

    protected void validateJob(String job) {
        Assert.isTrue(Pattern.matches(JOB_REGEXP, Objects.requireNonNull(job)), "올바르지 않은 직업입니다.");
    }
}
