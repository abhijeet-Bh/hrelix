package com.hrelix.app.employee;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    Optional<Employee> getEmployeeByEmail(String email);

    Optional<Employee> getEmployeeByPhone(String phone);

    Optional<Employee> getEmployeeById(UUID id);

    List<Employee> findByJoiningDateAfter(LocalDate date);
}
