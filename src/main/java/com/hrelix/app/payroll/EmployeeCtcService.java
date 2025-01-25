package com.hrelix.app.payroll;

import com.hrelix.app.employee.Employee;
import com.hrelix.app.employee.EmployeeRepository;
import com.hrelix.app.exceptions.EmployeeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeCtcService {

    @Autowired
    private CTCRepository ctcRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public EmployeeCTC createCtc(EmployeeCTC employeeCTC) {
        Optional<Employee> employee = employeeRepository.getEmployeeById(employeeCTC.getEmployee());
        if (employee.isEmpty())
            throw new EmployeeNotFoundException("Employee with id: " + employeeCTC.getEmployee() + " Not Found!");

        double netMonthlySalary = employeeCTC.getBasicPay()
                + employeeCTC.getOtherAllowance()
                + employeeCTC.getHouseRentAllowance()
                + employeeCTC.getSpecialAllowance();

        employeeCTC.setMonthlyNetCTC(netMonthlySalary);
        return ctcRepository.save(employeeCTC);
    }
}
