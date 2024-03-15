package com.homfo.user.adapter;

import com.homfo.user.entity.JpaUserMarketingAgreement;
import com.homfo.user.repository.UserMarketingAgreementRepository;
import com.homfo.user.command.RegisterCommand;
import com.homfo.user.dto.MarketingAgreementDto;
import com.homfo.user.dto.UserDto;
import com.homfo.user.dto.UserMarketingAgreementDto;
import com.homfo.user.port.LoadUserMarketingAgreementPort;
import com.homfo.user.port.ManageUserMarketingAgreementPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserMarketingAgreementPersistenceAdapter implements LoadUserMarketingAgreementPort, ManageUserMarketingAgreementPort {
    private final UserMarketingAgreementRepository userMarketingAgreementRepository;

    @Override
    public UserMarketingAgreementDto loadMarketingAgreement(UserDto userDto) {
        List<JpaUserMarketingAgreement> agreementList = userMarketingAgreementRepository.findByUserId(userDto.id());
        List<MarketingAgreementDto> marketingAgreementDtoList = agreementList.stream()
                .map(entity -> new MarketingAgreementDto(entity.getCode(), entity.isAgreement()))
                .toList();

        return new UserMarketingAgreementDto(userDto, marketingAgreementDtoList);
    }

    @Override
    public UserMarketingAgreementDto save(RegisterCommand command, UserDto userDto) {
        Set<MarketingAgreementDto> marketingCodeSet = new HashSet<>(command.marketingCodeList());
        List<JpaUserMarketingAgreement> agreements = new HashSet<>(command.marketingCodeList()).stream()
                .map(codeCommand -> JpaUserMarketingAgreement.builder()
                        .userId(userDto.id())
                        .code(codeCommand.code())
                        .agreement(codeCommand.isAgreement())
                        .build()
                )
                .toList();

        userMarketingAgreementRepository.saveAll(agreements);

        return new UserMarketingAgreementDto(userDto, marketingCodeSet.stream().toList());
    }
}
