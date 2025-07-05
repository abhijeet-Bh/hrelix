package com.hrelix.app.payroll;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PayrollRepo extends JpaRepository<Payroll, UUID> {

    @Query("SELECT p FROM Payroll p WHERE " +
            "(:status IS NULL OR p.status = :status) AND " +
            "(:month IS NULL OR EXTRACT(MONTH FROM p.payrollMonth) = :month) AND " +
            "(:year IS NULL OR EXTRACT(YEAR FROM p.payrollMonth) = :year)")
    List<Payroll> findByStatusAndMonthYear(
            @Param("status") PaymentStatus status,
            @Param("month") Integer month,
            @Param("year") Integer year
    );

    // ✅ FIX: Custom query required when employee is a UUID (not an Entity)
    @Query("SELECT p FROM Payroll p WHERE p.employee = :employeeId")
    List<Payroll> findByEmployee(@Param("employeeId") UUID employeeId);

    @Query("SELECT p FROM Payroll p WHERE EXTRACT(MONTH FROM p.payrollMonth) = :month AND EXTRACT(YEAR FROM p.payrollMonth) = :year AND p.employee = :employeeId")
    Optional<Payroll> findByPayrollMonthAndEmployee(
            @Param("month") int month,
            @Param("year") int year,
            @Param("employeeId") UUID employeeId
    );
}
