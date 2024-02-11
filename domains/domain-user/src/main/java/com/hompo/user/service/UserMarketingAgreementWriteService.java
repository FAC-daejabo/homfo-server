package com.hompo.user.service;

import com.hompo.user.command.RegisterCommand;
import com.hompo.user.dto.UserDto;
import com.hompo.user.dto.UserMarketingAgreementDto;

public interface UserMarketingAgreementWriteService {
    UserMarketingAgreementDto save(RegisterCommand command, UserDto userDto);
}
