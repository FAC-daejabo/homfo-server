package com.hompo.user.usecase;

import com.hompo.auth.dto.JwtDto;
import com.hompo.auth.dto.JwtSecretDto;
import com.hompo.auth.infra.util.JwtUtil;
import com.hompo.user.command.RegisterCommand;
import com.hompo.user.dto.UserDto;
import com.hompo.user.service.UserRefreshTokenWriteService;
import com.hompo.user.service.UserWriteService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 사용자 정보를 등록합니다.
 * */
public interface RegisterUsecase {
    /**
     * 사용자 정보를 등록하고 Jwt 정보를 얻습니다.
     *
     * 다음 작업을 한 트랜잭션으로 묶어야 합니다.
     *
     * 1. 사용자 정보 저장
     * 2. 사용자 리프레쉬 토큰 저장
     * 3. 사용자 마케팅 동의 여부 저장
     * */
    JwtDto execute(RegisterCommand command);
}
