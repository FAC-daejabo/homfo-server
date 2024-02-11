package com.hompo.user.usecase;

import com.hompo.user.dto.UserMarketingAgreementDto;

public interface GetUserInfoUsecase {
    UserMarketingAgreementDto execute(long userId);
}
