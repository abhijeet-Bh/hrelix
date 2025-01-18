package com.hrelix.app.admin;

import com.hrelix.app.employee.*;
import com.hrelix.app.exceptions.EmployeeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

@Service
public class AdminService {
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private EmployeeRepository employeeRepository;

    public void deleteEmployee(String id) throws Exception {
        Employee employee = employeeRepository.getEmployeeById(UUID.fromString(id))
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with ID " + id + " not found."));
        try {
            employeeRepository.delete(employee);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }


    public EmployeeDTO createEmployee(EmployeeDTO adminDTO) throws Exception {
        if (employeeRepository.getEmployeeByEmail(adminDTO.getEmail()).isPresent()
                || employeeRepository.getEmployeeByPhone(adminDTO.getPhone()).isPresent()) {
            throw new Exception("Employee with this email/phone already exists!");
        }
        Employee employee = EmployeeMapper.toEntity(adminDTO);
        String hashedPassword = passwordEncoder.encode(employee.getPassword());
        employee.setPassword(hashedPassword);
        employee.setJoiningDate(LocalDate.now());

        Employee createdEmployee = employeeRepository.save(employee);

        return EmployeeMapper.toDTO(createdEmployee);
    }

    public EmployeeDTO createAdmin(EmployeeDTO adminDTO) throws Exception {
        if (employeeRepository.count() > 0) {
            throw new Exception("Admin Already Exists!");
        }

        Employee employee = EmployeeMapper.toEntity(adminDTO);
        String hashedPassword = passwordEncoder.encode(employee.getPassword());
        employee.setPassword(hashedPassword);
        employee.setJoiningDate(LocalDate.now());

        employee.setRoles(Collections.singletonList(Role.ADMIN));
        Employee createdEmployee = employeeRepository.save(employee);

        return EmployeeMapper.toDTO(createdEmployee);
    }

    public EmployeeDTO updateRole(RoleDto roleDto, String id) throws Exception {
        Employee emp = employeeRepository.getEmployeeById(UUID.fromString(id))
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with ID " + id + " not found."));
        try {
            emp.setRoles(roleDto.getRoles());
            employeeRepository.save(emp);

            return EmployeeMapper.toDTO(emp);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }
}
