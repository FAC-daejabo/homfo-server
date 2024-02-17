package com.homfo.user.service;

import com.homfo.user.command.RegisterCommand;
import com.homfo.user.dto.UserDto;
import com.homfo.user.dto.UserMarketingAgreementDto;

/**
 * 사용자 마케팅 동의 정보를 DB에 저장합니다.
 * */
public interface UserMarketingAgreementWriteService {
    /**
     * 특정 사용자 마케팅 동의 정보를 DB에 저장합니다.
     * */
    UserMarketingAgreementDto save(RegisterCommand command, UserDto userDto);
}
