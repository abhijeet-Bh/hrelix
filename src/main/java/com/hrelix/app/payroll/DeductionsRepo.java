package com.hrelix.app.payroll;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeductionsRepo extends JpaRepository<Deductions, UUID> {
    Optional<Deductions> getDeductionByEmployee(UUID id);

    Optional<Deductions> findByEmployee(UUID id);
}
