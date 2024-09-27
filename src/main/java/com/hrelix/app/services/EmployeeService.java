package com.hrelix.app.services;

import com.hrelix.app.dtos.EmployeeDTO;
import com.hrelix.app.models.Employee;
import com.hrelix.app.models.mappers.EmployeeMapper;
import com.hrelix.app.repositories.EmployeeRepository;
import com.hrelix.app.utils.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Create a new employee
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = EmployeeMapper.toEntity(employeeDTO);
        Employee savedEmployee = employeeRepository.save(employee);
        return EmployeeMapper.toDTO(savedEmployee);
    }

    // Retrieve all employees (temporary function)
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream().map(EmployeeMapper::toDTO).collect(Collectors.toList());
    }

    // Retrieve employee by ID
    public EmployeeDTO getEmployeeById(UUID id) {
        return employeeRepository.findById(id)
                .map(EmployeeMapper::toDTO)
                .orElse(null);
    }
}