package com.homfo.user.service;

import com.homfo.user.dto.MarketingAgreementDto;
import com.homfo.user.dto.UserDto;
import com.homfo.user.dto.UserMarketingAgreementDto;
import com.homfo.user.entity.MySqlUserMarketingAgreement;
import com.homfo.user.repository.UserMarketingAgreementRepository;
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
