package com.homfo.user.response;

import com.homfo.enums.MarketingCode;
import com.homfo.user.dto.MarketingAgreementDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class MarketingAgreementResponse {
    @Schema(example = "MARKETING_CODE_00000001", description = "마케팅 코드")
    private final MarketingCode code;

    @Schema(example = "true", description = "동의 여부")
    private final boolean isAgreement;

    public MarketingAgreementResponse(MarketingAgreementDto dto) {
        this.code = dto.code();
        this.isAgreement = dto.isAgreement();
    }
}
