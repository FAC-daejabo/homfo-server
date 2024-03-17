package com.homfo.employee.attribute_converter;


import com.homfo.employee.infra.enums.EmployeeRole;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

/**
 * EmployeeRole enum을 DB 값과 매칭시킵니다.
 */
@Converter
@Slf4j
public class EmployeeRoleAttributeConverter implements AttributeConverter<EmployeeRole, String> {
    @Override
    public String convertToDatabaseColumn(EmployeeRole attribute) {
        return attribute.getCode();
    }

    @Override
    public EmployeeRole convertToEntityAttribute(String dbData) {
        return EmployeeRole.fromCode(dbData);
    }
}
