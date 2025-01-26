package com.hrelix.app.payroll;

import com.hrelix.app.employee.Employee;
import com.hrelix.app.employee.EmployeeRepository;
import com.hrelix.app.exceptions.DeductionNotFound;
import com.hrelix.app.exceptions.EmployeeCtcNotFound;
import com.hrelix.app.exceptions.EmployeeNotFoundException;
import com.hrelix.app.exceptions.PayrollNotFound;
import com.hrelix.app.leave.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @Autowired
    private MailService mailService;

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
        payroll.setNetDeductions(deductions.get().getTotalDeduction());
        payroll.setNetPayout(payroll.getNetCTC() - payroll.getNetDeductions());
        payroll.setStatus(PaymentStatus.PENDING);

        return payrollRepo.save(payroll);
    }

    public List<Payroll> getAllPayrolls() {
        return payrollRepo.findAll();
    }

    public List<Payroll> getPayrollsByStatus(PaymentStatus status, String monthYear) {
        return payrollRepo.findByStatusAndMonthYear(status, monthYear);
    }

    public Payroll getPayrollById(UUID payrollId) {
        Optional<Payroll> payroll = payrollRepo.findById(payrollId);
        if (payroll.isEmpty())
            throw new PayrollNotFound("Payroll with id: " + payrollId + " not found!");

        return payroll.get();
    }

    public List<Payroll> getPayrollByEmployeeId(UUID employeeId) {
        return payrollRepo.findByEmployee(employeeId);
    }


    // process payment

    public Payroll processPayment(UUID payrollId) {
        Optional<Payroll> payroll = payrollRepo.findById(payrollId);
        if (payroll.isEmpty())
            throw new PayrollNotFound("Payroll with id: " + payrollId + " not found!");

        Employee employee = employeeRepository.getEmployeeById(payroll.get().getEmployee()).get();
        EmployeeCTC employeeCTC = ctcRepository.getCtcByEmployee(payroll.get().getEmployee()).get();
        Deductions deductions = deductionsRepo.getDeductionByEmployee(payroll.get().getEmployee()).get();

        Payroll payrollPaid = payroll.get();
        payrollPaid.setStatus(PaymentStatus.PAID);
        payrollPaid.setDate(LocalDate.now());

        try {
            Payroll savedPayroll = payrollRepo.save(payrollPaid);
            mailService.sendPayrollEmail(
                    EmailDataDto.builder()
                            .month(payrollPaid.getMonthYear().split("-")[0])
                            .year(payrollPaid.getMonthYear().split("-")[1])
                            .empName(employee.getFirstName() + " " + employee.getLastName())
                            .baseSalary(String.valueOf(employeeCTC.getBasicPay()))
                            .allowances(
                                    String.valueOf(employeeCTC.getHouseRentAllowance()
                                            + employeeCTC.getOtherAllowance()
                                    )
                            )
                            .bonuses(String.valueOf(employeeCTC.getSpecialAllowance()))
                            .grossSalary(String.valueOf(employeeCTC.getMonthlyNetCTC()))
                            .taxDeductions(String.valueOf(deductions.getTds() + deductions.getProfessionalTax()))
                            .otherDeductions(String.valueOf(deductions.getOtherDeductions()))
                            .netSalary(String.valueOf(savedPayroll.getNetPayout()))
                            .bankName("Bank Of India")
                            .accountNumber("18XXX89XX891")
                            .IFSCCode("BXXIFSC00735")
                            .transactionID("TXNHRELIX00912")
                            .creditDateTime(String.valueOf(LocalDate.now()))
                            .build()
            );

            return savedPayroll;
        } catch (Exception e) {
            throw new RuntimeException("Failed to process the payroll!");
        }
    }
}
