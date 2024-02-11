package com.hompo.user.usecase;

import com.hompo.user.dto.UserMarketingAgreementDto;

/**
 * 사용자 계정 정보를 가져옵니다.
 * */
public interface GetUserInfoUsecase {
    /**
     * 특정 사용자 계정 정보를 가져옵니다.
     *
     * 마케팅 동의 여부도 제공합니다.
     * */
    UserMarketingAgreementDto execute(long userId);
}
