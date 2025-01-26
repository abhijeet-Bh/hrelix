package com.hrelix.app.payroll;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PayrollRepo extends JpaRepository<Payroll, UUID> {
    // Query for dynamic filtering
    @Query("SELECT p FROM Payroll p WHERE " +
            "(:status IS NULL OR p.status = :status) AND " +
            "(:monthYear IS NULL OR p.monthYear = :monthYear)")
    List<Payroll> findByStatusAndMonthYear(
            @Param("status") PaymentStatus status,
            @Param("monthYear") String monthYear
    );

    List<Payroll> findByEmployee(UUID employee);
}
