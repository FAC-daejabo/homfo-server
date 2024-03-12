package com.homfo.user.request;

import com.homfo.enums.MarketingCode;
import com.homfo.user.dto.MarketingAgreementDto;
import io.swagger.v3.oas.annotations.media.Schema;

public record MarketingAgreementRequest(
        @Schema(example = "MARKETING_CODE_00000001", description = "마케팅 코드")
        MarketingCode code,

        @Schema(example = "true", description = "동의 여부")
        boolean isAgreement
) {
    public MarketingAgreementDto toDto() {
        return new MarketingAgreementDto(code, isAgreement);
    }
}
