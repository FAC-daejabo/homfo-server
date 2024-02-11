package com.hompo.user.dto;

import java.util.List;

public record UserMarketingAgreementDto(
        UserDto userDto,

        List<MarketingAgreementDto> marketingAgreementList
) {
}
