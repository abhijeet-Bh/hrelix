package com.hrelix.app.payroll;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CTCRepository extends JpaRepository<EmployeeCTC, UUID> {
    Optional<EmployeeCTC> getCtcByEmployee(UUID id);

    Optional<EmployeeCTC> findByEmployee(UUID id);
}
