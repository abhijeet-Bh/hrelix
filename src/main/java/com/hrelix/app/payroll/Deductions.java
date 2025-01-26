package com.hrelix.app.payroll;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Deductions {
    @Id
    @GeneratedValue
    private UUID id;

    private UUID employee;
    private double epf;
    private double professionalTax;
    private double tds;
    private double otherDeductions;
    private double totalDeduction;
}
