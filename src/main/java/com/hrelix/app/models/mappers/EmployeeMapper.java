package com.hrelix.app.models.mappers;


import com.hrelix.app.dtos.EmployeeDTO;
import com.hrelix.app.models.Employee;

public class EmployeeMapper {

    public static EmployeeDTO toDTO(Employee employee) {
        return new EmployeeDTO(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getPhone(),
                employee.getSalary(),
                employee.getJoiningDate(),
                employee.getPassword(),
                employee.getRoles()
        );
    }

    public static Employee toEntity(EmployeeDTO employeeDTO) {
        return new Employee(
                employeeDTO.getId(),
                employeeDTO.getFirstName(),
                employeeDTO.getLastName(),
                employeeDTO.getEmail(),
                employeeDTO.getPhone(),
                employeeDTO.getSalary(),
                employeeDTO.getJoiningDate(),
                employeeDTO.getPassword(),
                employeeDTO.getRoles()
        );
    }
}