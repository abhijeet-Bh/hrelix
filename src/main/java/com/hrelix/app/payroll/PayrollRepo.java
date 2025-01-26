package com.hrelix.app.payroll;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PayrollRepo extends JpaRepository<Payroll, UUID> {
    List<Payroll> getPayrollsByStatus(PaymentStatus status);
}
