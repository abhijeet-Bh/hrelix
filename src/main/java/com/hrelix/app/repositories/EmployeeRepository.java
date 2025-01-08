package com.hrelix.app.repositories;


import com.hrelix.app.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    Optional<Employee> getEmployeeByEmail(String email);

    Optional<Employee> getEmployeeByPhone(String phone);

    Employee getEmployeeById(UUID id);
}
