package com.hrelix.app.payroll;

import com.hrelix.app.employee.Employee;
import com.hrelix.app.employee.EmployeeRepository;
import com.hrelix.app.exceptions.DeductionNotFound;
import com.hrelix.app.exceptions.EmployeeCtcNotFound;
import com.hrelix.app.exceptions.EmployeeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PayrollService {

    @Autowired

    private PayrollRepo payrollRepo;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CTCRepository ctcRepository;

    @Autowired
    private DeductionsRepo deductionsRepo;

    public Payroll createDeduction(Payroll payroll) {
        Optional<Employee> employee = employeeRepository.getEmployeeById(payroll.getEmployee());
        if (employee.isEmpty())
            throw new EmployeeNotFoundException(
                    "Employee with id: " + payroll.getEmployee() + " Not Found!");

        Optional<Deductions> deductions = deductionsRepo.getDeductionByEmployee(payroll.getEmployee());
        if (deductions.isEmpty())
            throw new DeductionNotFound(
                    "Deduction for employee id: " + payroll.getEmployee() + " Not Found!");

        Optional<EmployeeCTC> employeeCTC = ctcRepository.getCtcByEmployee(payroll.getEmployee());
        if (employeeCTC.isEmpty())
            throw new EmployeeCtcNotFound(
                    "CTC for employee id: " + payroll.getEmployee() + " Not Found!");

        payroll.setNetCTC(employeeCTC.get().getMonthlyNetCTC());
        payroll.setNetCTC(deductions.get().getTotalDeduction());
        payroll.setNetPayout(payroll.getNetCTC() - payroll.getNetDeductions());
        payroll.setStatus(PaymentStatus.PENDING);

        return payrollRepo.save(payroll);
    }

    public List<Payroll> getAllPayrolls() {
        return payrollRepo.findAll();
    }

    public List<Payroll> getPayrollsByStatus(PaymentStatus status) {
        return payrollRepo.getPayrollsByStatus(status);
    }
}
