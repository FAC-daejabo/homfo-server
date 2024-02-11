package com.hompo.user.service;

import com.hompo.user.command.RegisterCommand;
import com.hompo.user.dto.UserDto;
import com.hompo.user.dto.UserMarketingAgreementDto;

/**
 * 사용자 마케팅 동의 정보를 DB에 저장합니다.
 * */
public interface UserMarketingAgreementWriteService {
    /**
     * 특정 사용자 마케팅 동의 정보를 DB에 저장합니다.
     * */
    UserMarketingAgreementDto save(RegisterCommand command, UserDto userDto);
}
