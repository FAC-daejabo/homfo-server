package com.hompo.user.service;

import com.hompo.user.dto.UserDto;
import com.hompo.user.dto.UserMarketingAgreementDto;

public interface UserMarketingAgreementReadService {
     UserMarketingAgreementDto getUserMarketingInfo(UserDto userDto);
}
