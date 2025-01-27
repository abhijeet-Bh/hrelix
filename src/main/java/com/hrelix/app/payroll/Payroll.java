package com.hrelix.app.payroll;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payroll {
    @Id
    @GeneratedValue
    private UUID payrollId;

    @NotNull(message = "Employee Id is Needed to create payroll")
    private UUID employee;
    @Column(nullable = false)
    private String monthYear;

    private double netCTC;
    private double netDeductions;
    private double netPayout;

    // test email for testing purpose
    private String testEmail;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private LocalDate date;
}
