package com.hrelix.app.payroll;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BankAccountDetailRepo extends JpaRepository<BankAccountDetail, UUID> {
    Optional<BankAccountDetail> findByEmployeeId(UUID employeeId);
}
