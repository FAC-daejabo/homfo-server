package com.hompo.user.usecase;

import com.hompo.auth.dto.JwtSecretDto;
import com.hompo.user.service.UserRefreshTokenWriteService;
import com.hompo.user.service.UserWriteService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


public interface DeleteAccountUsecase {
    void execute(long userId);
}
