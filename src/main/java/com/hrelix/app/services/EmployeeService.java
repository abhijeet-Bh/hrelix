package com.hrelix.app.services;

import com.hrelix.app.dtos.EmployeeDTO;
import com.hrelix.app.models.Employee;
import com.hrelix.app.models.mappers.EmployeeMapper;
import com.hrelix.app.models.utils.Role;
import com.hrelix.app.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private EmployeeRepository employeeRepository;

    // Create a new employee
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = EmployeeMapper.toEntity(employeeDTO);
        String hashedPassword = passwordEncoder.encode(employee.getPassword());
        employee.setPassword(hashedPassword);
        employee.setJoiningDate(LocalDate.now());

        employee.setRoles(Collections.singletonList(Role.EMPLOYEE));

        Employee savedEmployee = employeeRepository.save(employee);
        return EmployeeMapper.toDTO(savedEmployee);
    }

    // Retrieve all employees (temporary function)
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream().map(EmployeeMapper::toDTO).collect(Collectors.toList());
    }

    // Retrieve employee by Email
    public Employee findByEmail(String email) throws Exception {
        Optional<Employee> employee = employeeRepository.getEmployeeByEmail(email);
        if (employee.isPresent()) {
            return employee.get();
        } else {
            throw new Exception("Employee Not Found!");
        }
    }

    // Retrieve employee by Email
    public Employee findById(UUID id) {
        return employeeRepository.getEmployeeByid(id);
    }

    public boolean deleteEmployee(Employee employee) {
        try {
            employeeRepository.delete(employee);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public EmployeeDTO updateEmployee(EmployeeDTO employee, UUID id) {
        Employee emp = employeeRepository.getEmployeeByid(id);
        if (emp == null) {
            throw new IllegalArgumentException("Employee Not Found!");
        } else {
            emp.setFirstName(employee.getFirstName());
            emp.setLastName(employee.getLastName());
            emp.setSalary(employee.getSalary());
            Employee updated = employeeRepository.save(emp);

            return EmployeeDTO.builder()
                    .id(updated.getId())
                    .firstName(updated.getFirstName())
                    .lastName(updated.getLastName())
                    .email(updated.getEmail())
                    .phone(updated.getPhone())
                    .salary(updated.getSalary())
                    .roles(updated.getRoles())
                    .joiningDate(updated.getJoiningDate())
                    .build();
        }
    }

    public EmployeeDTO getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get user details from the authentication object
        if (authentication != null && authentication.isAuthenticated()) {
            Employee principal = (Employee) authentication.getPrincipal();

            return EmployeeDTO.builder()
                    .id(principal.getId())
                    .firstName(principal.getFirstName())
                    .lastName(principal.getLastName())
                    .email(principal.getEmail())
                    .phone(principal.getPhone())
                    .salary(principal.getSalary())
                    .joiningDate(principal.getJoiningDate())
                    .roles(principal.getRoles())
                    .build();
        }

        return new EmployeeDTO();
    }
}