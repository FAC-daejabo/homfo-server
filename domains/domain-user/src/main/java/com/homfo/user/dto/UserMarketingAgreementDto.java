package com.homfo.user.dto;

import java.util.List;

/**
 * 마케팅 동의 여부를 포함한 사용자 정보 DTO 입니다.
 * */
public record UserMarketingAgreementDto(
        UserDto userDto,

        List<MarketingAgreementDto> marketingAgreementList
) {
}
