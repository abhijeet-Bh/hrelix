package com.hrelix.app.services;

import com.hrelix.app.dtos.EmployeeDTO;
import com.hrelix.app.models.Employee;
import com.hrelix.app.models.mappers.EmployeeMapper;
import com.hrelix.app.repositories.EmployeeRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Create a new employee
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = EmployeeMapper.toEntity(employeeDTO);
        String hashedPassword = passwordEncoder.encode(employee.getPassword());
        employee.setPassword(hashedPassword);
        Employee savedEmployee = employeeRepository.save(employee);
        return EmployeeMapper.toDTO(savedEmployee);
    }

    // Retrieve all employees (temporary function)
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream().map(EmployeeMapper::toDTO).collect(Collectors.toList());
    }

    // Retrieve employee by Email
    public Employee findByEmail(String email) throws Exception{
        Optional<Employee> employee =  employeeRepository.getEmployeeByEmail(email);
        if(employee.isPresent()){
            return employee.get();
        }else{
            throw new Exception("Employee Not Found!");
        }
    }

    // Retrieve employee by Email
    public Employee findById(UUID id) {
        return employeeRepository.getEmployeeByid(id);
    }
}