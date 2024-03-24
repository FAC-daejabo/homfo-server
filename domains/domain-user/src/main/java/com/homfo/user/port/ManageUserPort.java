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
}
