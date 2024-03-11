package com.homfo.employee.infra.attribute_converter;


import com.homfo.employee.infra.enums.EmployeeStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

/**
 * EmployeeStatus enum을 DB 값과 매칭시킵니다.
 */
@Converter
@Slf4j
public class EmployeeStatusAttributeConverter implements AttributeConverter<EmployeeStatus, String> {

    @Override
    public String convertToDatabaseColumn(EmployeeStatus attribute) {
        return attribute.getCode();
    }

    @Override
    public EmployeeStatus convertToEntityAttribute(String dbData) {
        return EmployeeStatus.fromCode(dbData);
    }
}
