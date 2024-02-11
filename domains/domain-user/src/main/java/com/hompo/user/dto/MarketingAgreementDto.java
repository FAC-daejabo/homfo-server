package com.hompo.user.dto;

import com.hompo.enums.MarketingCode;
import io.swagger.v3.oas.annotations.media.Schema;

public record MarketingAgreementDto(
        @Schema(example = "MARKETING_CODE_00000001", description = "마케팅 코드")
        MarketingCode code,

        @Schema(example = "true", description = "동의 여부")
        boolean isAgreement
) {
}
