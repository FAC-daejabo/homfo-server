package com.homfo.user.usecase;

import com.homfo.user.dto.UserMarketingAgreementDto;

/**
 * 사용자 계정 정보를 가져옵니다.
 * */
public interface GetUserInfoUsecase {
    /**
     * 특정 사용자 계정 정보를 가져옵니다.
     *
     * 마케팅 동의 여부도 제공합니다.
     *
     * @throws com.homfo.error.ResourceNotFoundException 존재하지 않는 사용자라면
     * */
    UserMarketingAgreementDto getUserInfo(long userId);
}
