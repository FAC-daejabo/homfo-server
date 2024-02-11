package com.hompo.user.usecase;

import com.hompo.user.dto.UserDto;
import com.hompo.user.dto.UserMarketingAgreementDto;
import com.hompo.user.service.UserMarketingAgreementReadService;
import com.hompo.user.service.UserMarketingAgreementWriteService;
import com.hompo.user.service.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MySqlGetUserInfoUsecase implements GetUserInfoUsecase {
    private final UserReadService userReadService;

    private final UserMarketingAgreementReadService userMarketingAgreementReadService;

    @Override
    public UserMarketingAgreementDto execute(long userId) {
        UserDto userDto = userReadService.findById(userId);
        return userMarketingAgreementReadService.getUserMarketingInfo(userDto);
    }
}
