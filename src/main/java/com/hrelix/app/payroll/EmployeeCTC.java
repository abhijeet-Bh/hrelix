package com.hrelix.app.payroll;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Builder
public class EmployeeCTC {
    @Id
    @GeneratedValue
    private UUID id;

    private UUID employee;
    private double basicPay;
    private double houseRentAllowance;
    private double specialAllowance;
    private double otherAllowance;
    private double monthlyNetCTC;
}
