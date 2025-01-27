package com.hrelix.app.payroll;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class BankAccountDetail {
    @Id
    @GeneratedValue
    private UUID id;

    private UUID employeeId;
    private String bankName;
    private String bankAccountNumber;
    private String ifscCode;
}
