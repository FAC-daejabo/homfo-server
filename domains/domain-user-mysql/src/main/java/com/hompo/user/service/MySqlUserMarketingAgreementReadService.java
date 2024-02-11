package com.hompo.user.service;

import com.hompo.user.dto.MarketingAgreementDto;
import com.hompo.user.dto.UserDto;
import com.hompo.user.dto.UserMarketingAgreementDto;
import com.hompo.user.entity.MySqlUserMarketingAgreement;
import com.hompo.user.repository.UserMarketingAgreementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MySqlUserMarketingAgreementReadService implements UserMarketingAgreementReadService{
    private final UserMarketingAgreementRepository userMarketingAgreementRepository;

    public UserMarketingAgreementDto getUserMarketingInfo(UserDto userDto) {
        List<MySqlUserMarketingAgreement> agreementList = userMarketingAgreementRepository.findByUserId(userDto.id());
        List<MarketingAgreementDto> marketingAgreementDtoList = agreementList.stream()
                .map(entity -> new MarketingAgreementDto(entity.getCode(), entity.isAgreement()))
                .toList();

        return new UserMarketingAgreementDto(userDto, marketingAgreementDtoList);
    }
}
