package com.homfo.user.response;

import com.homfo.user.dto.UserMarketingAgreementDto;
import lombok.Getter;

import java.util.List;

/**
 * 마케팅 동의 여부를 포함한 사용자 정보 DTO 입니다.
 * */
@Getter
public class UserMarketingAgreementResponse {
    private final UserInfoResponse user;

    private final List<MarketingAgreementResponse> marketingAgreementList;

    public UserMarketingAgreementResponse(UserMarketingAgreementDto dto) {
        this.user = new UserInfoResponse(dto.userDto());
        this.marketingAgreementList = dto.marketingAgreementList().stream().map(MarketingAgreementResponse::new).toList();
    }
}
