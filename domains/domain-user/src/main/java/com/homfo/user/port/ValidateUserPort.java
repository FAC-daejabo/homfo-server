package com.homfo.user.port;

import lombok.NonNull;

/**
 * 사용자와 관련된 정보를 확인합니다.
 * */
public interface ValidateUserPort {
    /**
     * 특정 계정이 존재하는지 확인합니다.
     * */
    boolean existAccount(@NonNull String account);

    /**
     * 특정 닉네임이 존재하는지 확인합니다.
     * */
    boolean existNickname(@NonNull String nickname);

    /**
     * 특정 전화번호가 존재하는지 확인합니다.
     * */
    boolean existPhoneNumber(@NonNull String phoneNumber);
}
