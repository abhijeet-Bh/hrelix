package com.hrelix.app.models.mappers;

import com.hrelix.app.dtos.EmployeeDTO;
import com.hrelix.app.models.Employee;
import com.hrelix.app.models.EmployeeMapper;
import com.hrelix.app.models.Role;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmployeeMapperTest {

    @BeforeAll
    static void beforeAll() {
        System.out.println("Instantiating Employee mapper test ..... ");
    }

    @Test
    public void requestBodyEmployeeDTOToDatabaseEmployee() {

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

        System.out.println("Testing for requestBody Employee ..... ");
        System.out.println(reqBodyEmployee.getFirstName());


        Employee newEmployee = EmployeeMapper.toEntity(reqBodyEmployee);

        assertEquals(reqBodyEmployee.getFirstName(), newEmployee.getFirstName());
        assertEquals(reqBodyEmployee.getLastName(), newEmployee.getLastName());
        assertEquals(reqBodyEmployee.getEmail(), newEmployee.getEmail());
        assertEquals(reqBodyEmployee.getPhone(), newEmployee.getPhone());
        assertEquals(reqBodyEmployee.getSalary(), newEmployee.getSalary());
        assertEquals(reqBodyEmployee.getJoiningDate(), newEmployee.getJoiningDate());
        assertEquals(reqBodyEmployee.getPassword(), newEmployee.getPassword());
        assertEquals(reqBodyEmployee.getRoles(), newEmployee.getRoles());

        System.out.println("Test for requestBody Employee to database Employee object successful :)");
    }
}