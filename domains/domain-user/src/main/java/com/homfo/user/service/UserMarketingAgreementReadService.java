package com.homfo.user.service;

import com.homfo.user.dto.UserDto;
import com.homfo.user.dto.UserMarketingAgreementDto;

/**
 * 사용자 마케팅 동의 정보를 DB로부터 읽어옵니다.
 * */
public interface UserMarketingAgreementReadService {
     /**
      * 특정 사용자 마케팅 동의 정보를 DB로부터 읽어옵니다.
      * */
     UserMarketingAgreementDto getUserMarketingInfo(UserDto userDto);
}
