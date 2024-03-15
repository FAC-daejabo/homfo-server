package com.homfo.user.port;

import com.homfo.user.command.RegisterCommand;
import com.homfo.user.dto.UserDto;
import lombok.NonNull;

public interface ManageUserAccountPort {
    /**
     * 회원가입합니다.
     */
    UserDto register(@NonNull RegisterCommand command);

    /**
     * 회원탈퇴합니다.
     */
    void deleteAccount(long userId);
}
