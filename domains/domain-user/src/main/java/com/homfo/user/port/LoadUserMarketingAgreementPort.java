package com.homfo.user.port;

import com.homfo.user.dto.UserDto;
import com.homfo.user.dto.UserMarketingAgreementDto;

public interface LoadUserMarketingAgreementPort {
    /**
     * 특정 사용자 마케팅 동의 정보를 DB로부터 읽어옵니다.
     * */
    UserMarketingAgreementDto loadMarketingAgreement(UserDto userDto);
}
