package com.homfo.user.usecase;

import com.homfo.auth.dto.JwtDto;
import com.homfo.user.command.RegisterCommand;

/**
 * 직원 정보를 등록합니다.
 * */
public interface RegisterUsecase {
    /**
     * 직원 정보를 등록하고 Jwt 정보를 얻습니다.
     *
     * 다음 작업을 한 트랜잭션으로 묶어야 합니다.
     *
     * 1. 직원 정보 저장
     * 2. 직원 리프레쉬 토큰 저장
     * 3. 직원 마케팅 동의 여부 저장
     *
     * @throws com.homfo.error.ResourceAlreadyExistException 이미 존재하는 직원 정보라면
     * */
    JwtDto register(RegisterCommand command);
}
