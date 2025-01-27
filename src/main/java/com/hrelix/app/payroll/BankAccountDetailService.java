package com.hrelix.app.payroll;

import com.hrelix.app.employee.Employee;
import com.hrelix.app.employee.EmployeeRepository;
import com.hrelix.app.exceptions.EmployeeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BankAccountDetailService {

    @Autowired
    BankAccountDetailRepo bankAccountDetailRepo;

    @Autowired
    EmployeeRepository employeeRepository;

    public BankAccountDetail createBankDetails(BankAccountDetail accountDetail) {
        Optional<Employee> employee = employeeRepository.getEmployeeById(accountDetail.getEmployeeId());
        if (employee.isEmpty())
            throw new EmployeeNotFoundException("Employee with id: " + accountDetail.getEmployeeId() + " not Found!");

        return bankAccountDetailRepo.save(accountDetail);
    }
}
