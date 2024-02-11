package com.hompo.user.service;

import com.hompo.enums.MarketingCode;
import com.hompo.user.command.RegisterCommand;
import com.hompo.user.dto.MarketingAgreementDto;
import com.hompo.user.dto.UserDto;
import com.hompo.user.dto.UserMarketingAgreementDto;
import com.hompo.user.entity.MySqlUserMarketingAgreement;
import com.hompo.user.repository.UserMarketingAgreementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class MySqlUserMarketingAgreementWriteService implements UserMarketingAgreementWriteService {
    private final UserMarketingAgreementRepository userMarketingAgreementRepository;

    @Override
    public UserMarketingAgreementDto save(RegisterCommand command, UserDto userDto) {
        Set<MarketingAgreementDto> marketingCodeSet = new HashSet<>(command.marketingCodeList());
        List<MySqlUserMarketingAgreement> agreements = new HashSet<>(command.marketingCodeList()).stream()
                .map(codeCommand -> MySqlUserMarketingAgreement.builder()
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
