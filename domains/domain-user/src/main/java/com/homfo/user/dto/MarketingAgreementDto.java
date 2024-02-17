package com.homfo.user.dto;

import com.homfo.enums.MarketingCode;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 마케팅 동의 여부 DTO 입니다.
 * */
public record MarketingAgreementDto(
        @Schema(example = "MARKETING_CODE_00000001", description = "마케팅 코드")
        MarketingCode code,

        @Schema(example = "true", description = "동의 여부")
        boolean isAgreement
) {
}
