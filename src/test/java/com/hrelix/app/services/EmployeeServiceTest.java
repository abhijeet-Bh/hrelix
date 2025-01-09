package com.hrelix.app.services;

import com.hrelix.app.employee.EmployeeDTO;
import com.hrelix.app.employee.Employee;
import com.hrelix.app.employee.EmployeeMapper;
import com.hrelix.app.employee.EmployeeService;
import com.hrelix.app.employee.Role;
import com.hrelix.app.employee.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeService employeeService;


    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEmployee_Success() {
        List<Role> roles = new ArrayList<>();
        roles.add(Role.EMPLOYEE);
        roles.add(Role.ADMIN);

        EmployeeDTO reqBodyEmployee = new EmployeeDTO(
                UUID.randomUUID(),
                "Abhijeet",
                "Bhardwaj",
                "email@test.com",
                "6201240770",
                5000000.00,
                LocalDate.now(),
                "testPassword",
                roles
        );

        Employee databaseEmployee = new Employee(
                reqBodyEmployee.getId(),
                "Abhijeet",
                "Bhardwaj",
                "email@test.com",
                "6201240770",
                5000000.00,
                LocalDate.now(),
                "hashedPassword",
                roles
        );

        // Mock the EmployeeMapper behavior
        MockedStatic<EmployeeMapper> mockedEmployeeMapper = mockStatic(EmployeeMapper.class);
        mockedEmployeeMapper.when(() -> EmployeeMapper.toEntity(reqBodyEmployee)).thenReturn(databaseEmployee);
        mockedEmployeeMapper.when(() -> EmployeeMapper.toDTO(databaseEmployee)).thenReturn(reqBodyEmployee);

        //Mock-Calls
        when(EmployeeMapper.toEntity(reqBodyEmployee)).thenReturn(databaseEmployee);
        // Mock password encoder behavior
        when(passwordEncoder.encode(reqBodyEmployee.getPassword())).thenReturn("hashedPassword");
        when(employeeRepository.save(databaseEmployee)).thenReturn(databaseEmployee);

        //When
        EmployeeDTO savedEmp = employeeService.createEmployee(reqBodyEmployee);

        // Verify interactions with EmployeeMapper static methods
        mockedEmployeeMapper.verify(() -> EmployeeMapper.toEntity(reqBodyEmployee));
        mockedEmployeeMapper.verify(() -> EmployeeMapper.toDTO(databaseEmployee));

        // Verify interactions with other mocks
        System.out.println("Testing if saved password was hashed .... ");
        String encodedPassword = passwordEncoder.encode(reqBodyEmployee.getPassword());
        assertNotEquals(reqBodyEmployee.getPassword(), encodedPassword);

        System.out.println("reqBodyEmployee password: " + reqBodyEmployee.getPassword());
        System.out.println("databaseEmployee password: " + databaseEmployee.getPassword());
        System.out.println("savedEmp employee password: " + savedEmp.getPassword());

        //Assert
        System.out.println("testing for saved employee ... ");
        assertNotNull(savedEmp);
        assertEquals(reqBodyEmployee, savedEmp);
        System.out.println("employee " + savedEmp.getFirstName() + " saved successfully!");

        System.out.println("Testing for all fields that are saved .... ");
        assertEquals(reqBodyEmployee.getFirstName(), savedEmp.getFirstName());
        assertEquals(reqBodyEmployee.getLastName(), savedEmp.getLastName());
        assertEquals(reqBodyEmployee.getId(), savedEmp.getId());

        System.out.println("All tests passed successfully :)");
    }
}