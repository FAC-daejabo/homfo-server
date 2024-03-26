package com.homfo.user.port;

import com.homfo.user.command.RegisterCommand;
import com.homfo.user.dto.UserDto;
import lombok.NonNull;

public interface ManageUserPort {
    /**
     * 회원가입합니다.
     *
     * @throws com.homfo.error.ResourceAlreadyExistException 이미 존재하는 사용자 정보라면
     */
    UserDto register(@NonNull RegisterCommand command);

    /**
     * 회원탈퇴합니다.
     *
     * @throws com.homfo.error.ResourceNotFoundException 존재하지 않는 사용자라면
     */
    void deleteAccount(long userId);

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
