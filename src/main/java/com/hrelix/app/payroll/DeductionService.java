package com.hrelix.app.payroll;

import com.hrelix.app.employee.Employee;
import com.hrelix.app.employee.EmployeeRepository;
import com.hrelix.app.exceptions.DeductionNotFound;
import com.hrelix.app.exceptions.EmployeeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class DeductionService {

    @Autowired
    private DeductionsRepo deductionsRepo;

    @Autowired
    private EmployeeRepository employeeRepository;


    public Deductions createDeduction(Deductions deduction) {
        Optional<Employee> employee = employeeRepository.getEmployeeById(deduction.getEmployee());
        if (employee.isEmpty())
            throw new EmployeeNotFoundException("Employee with id: " + deduction.getEmployee() + " Not Found!");

        double totalDeduction = deduction.getEpf()
                + deduction.getProfessionalTax()
                + deduction.getTds()
                + deduction.getOtherDeductions();

        deduction.setTotalDeduction(totalDeduction);
        return deductionsRepo.save(deduction);
    }

    public Deductions findByEmployeeId(UUID id) {
        return deductionsRepo.findByEmployee(id)
                .orElseThrow(() -> new DeductionNotFound("Deduction with employee id: " + id + " Not Found!"));
    }
}
