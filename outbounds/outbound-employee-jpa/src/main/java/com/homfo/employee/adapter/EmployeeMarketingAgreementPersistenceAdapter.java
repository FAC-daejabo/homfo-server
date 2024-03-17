package com.homfo.employee.adapter;

import com.homfo.employee.command.RegisterCommand;
import com.homfo.employee.dto.EmployeeDto;
import com.homfo.employee.dto.EmployeeMarketingAgreementDto;
import com.homfo.employee.dto.MarketingAgreementDto;
import com.homfo.employee.entity.JpaEmployeeMarketingAgreement;
import com.homfo.employee.port.LoadEmployeeMarketingAgreementPort;
import com.homfo.employee.port.ManageEmployeeMarketingAgreementPort;
import com.homfo.employee.repository.EmployeeMarketingAgreementRepository;
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
