package com.hrelix.app.employee;

import com.hrelix.app.exceptions.EmployeeNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
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

        try {
            Employee savedEmployee = employeeRepository.save(employee);
            return EmployeeMapper.toDTO(savedEmployee);
        } catch (Exception e) {
            throw new EmployeeNotFoundException(e.getMessage());
        }
    }

    // Retrieve all employees (temporary function)
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream().map(EmployeeMapper::toDTO).collect(Collectors.toList());
    }

    // Retrieve employee by Email
    public Employee findByEmail(String email) throws Exception {
        return employeeRepository.getEmployeeByEmail(email)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with email " + email + " not found."));
    }

    // Retrieve employee by Email
    public Employee findById(UUID id) {
        return employeeRepository.getEmployeeById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with ID " + id + " not found."));
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
        Employee emp = employeeRepository.getEmployeeById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with ID " + id + " not found."));
        emp.setFirstName(employee.getFirstName());
        emp.setLastName(employee.getLastName());
        emp.setSalary(employee.getSalary());
        Employee updated = employeeRepository.save(emp);

        return EmployeeDTO.builder()
                .id(updated.getId())
                .firstName(updated.getFirstName())
                .lastName(updated.getLastName())
                .avatar(updated.getAvatar())
                .email(updated.getEmail())
                .phone(updated.getPhone())
                .salary(updated.getSalary())
                .roles(updated.getRoles())
                .joiningDate(updated.getJoiningDate())
                .build();
    }

    public EmployeeDTO getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get user details from the authentication object
        if (authentication != null && authentication.isAuthenticated()) {
            try {
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
                        .avatar(principal.getAvatar())
                        .build();
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new RuntimeException("Something went wrong");
            }
        } else {
            return null;
        }
    }

    public List<Employee> getNewEmployees() {
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        return employeeRepository.findByJoiningDateAfter(thirtyDaysAgo);
    }

    public EmployeeDTO updateEmployeeAvatar(Employee employee) {
        Employee emp = employeeRepository.save(employee);
        return EmployeeDTO.builder()
                .id(emp.getId())
                .firstName(emp.getFirstName())
                .lastName(emp.getLastName())
                .email(emp.getEmail())
                .phone(emp.getPhone())
                .salary(emp.getSalary())
                .joiningDate(emp.getJoiningDate())
                .roles(emp.getRoles())
                .avatar(emp.getAvatar())
                .build();
    }

    public List<EmployeeDTO> searchEmployeeByEmailOrName(String key) {
        List<Employee> employees = employeeRepository.findByEmailIgnoreCaseOrFirstNameIgnoreCaseOrLastNameIgnoreCase(key, key, key);
        return employees.stream()
                .map(EmployeeMapper::toDTO)
                .collect(Collectors.toList());
    }
}