package com.homfo.user.adapter;

import com.homfo.user.command.RegisterCommand;
import com.homfo.user.dto.EmployeeDto;
import com.homfo.user.dto.EmployeeMarketingAgreementDto;
import com.homfo.user.dto.MarketingAgreementDto;
import com.homfo.user.entity.JpaEmployeeMarketingAgreement;
import com.homfo.user.port.LoadEmployeeMarketingAgreementPort;
import com.homfo.user.port.ManageEmployeeMarketingAgreementPort;
import com.homfo.user.repository.EmployeeMarketingAgreementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class EmployeeMarketingAgreementPersistenceAdapter implements LoadEmployeeMarketingAgreementPort, ManageEmployeeMarketingAgreementPort {
    private final EmployeeMarketingAgreementRepository employeeMarketingAgreementRepository;

    @Override
    public EmployeeMarketingAgreementDto loadMarketingAgreement(EmployeeDto employeeDto) {
        List<JpaEmployeeMarketingAgreement> agreementList = employeeMarketingAgreementRepository.findByEmployeeId(employeeDto.id());
        List<MarketingAgreementDto> marketingAgreementDtoList = agreementList.stream()
                .map(entity -> new MarketingAgreementDto(entity.getCode(), entity.isAgreement()))
                .toList();

        return new EmployeeMarketingAgreementDto(employeeDto, marketingAgreementDtoList);
    }

    @Override
    public EmployeeMarketingAgreementDto save(RegisterCommand command, EmployeeDto employeeDto) {
        Set<MarketingAgreementDto> marketingCodeSet = new HashSet<>(command.marketingCodeList());
        List<JpaEmployeeMarketingAgreement> agreements = new HashSet<>(command.marketingCodeList()).stream()
                .map(codeCommand -> JpaEmployeeMarketingAgreement.builder()
                        .employeeId(employeeDto.id())
                        .code(codeCommand.code())
                        .agreement(codeCommand.isAgreement())
                        .build()
                )
                .toList();

        employeeMarketingAgreementRepository.saveAll(agreements);

        return new EmployeeMarketingAgreementDto(employeeDto, marketingCodeSet.stream().toList());
    }
}
