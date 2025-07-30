package com.hrelix.app.employee;


public class EmployeeMapper {

    public static EmployeeDTO toDTO(Employee employee) {
        return EmployeeDTO.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .salary(employee.getSalary())
                .joiningDate(employee.getJoiningDate())
                .avatar(employee.getAvatar())
                .team(employee.getTeam())
                .position(employee.getPosition())
                .roles(employee.getRoles())
                .build();
    }

    public static Employee toEntity(EmployeeDTO employeeDTO) {
        return new Employee(
                employeeDTO.getId(),
                employeeDTO.getFirstName(),
                employeeDTO.getLastName(),
                employeeDTO.getEmail(),
                employeeDTO.getPhone(),
                "https://hrelix-backend.s3.ap-south-1.amazonaws.com/profiles/default-avatar.png",
                employeeDTO.getSalary(),
                employeeDTO.getJoiningDate(),
                employeeDTO.getPassword(),
                employeeDTO.getPosition(),
                employeeDTO.getTeam(),
                employeeDTO.getRoles()
        );
    }
}