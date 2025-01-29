package com.hrelix.app.payroll;

import com.hrelix.app.employee.Employee;
import com.hrelix.app.employee.EmployeeRepository;
import com.hrelix.app.exceptions.*;
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

    @Autowired
    private BankAccountDetailRepo bankAccountDetailRepo;

    public Payroll createDeduction(Payroll payroll) {
        Optional<Payroll> existingPayroll = payrollRepo.findByMonthYearAndEmployee(payroll.getMonthYear(), payroll.getEmployee());
        if (existingPayroll.isPresent())
            throw new RuntimeException("Payroll already exists!");

        EmployeeCTC employeeCTC = ctcRepository.getCtcByEmployee(payroll.getEmployee())
                .orElseThrow(() -> new EmployeeCtcNotFound("CTC for employee id: " + payroll.getEmployee() + " Not Found!"));

        Deductions deductions = deductionsRepo.getDeductionByEmployee(payroll.getEmployee())
                .orElseThrow(() -> new DeductionNotFound("Deduction for employee id: " + payroll.getEmployee() + " Not Found!"));

        payroll.setNetCTC(employeeCTC.getMonthlyNetCTC());
        payroll.setNetDeductions(deductions.getTotalDeduction());
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

    public Payroll processPayment(UUID payrollId, String testEmail, String txnNum) {
        Payroll payroll = payrollRepo.findById(payrollId)
                .orElseThrow(() -> new PayrollNotFound("Payroll with id: " + payrollId + " not found!"));

        if (payroll.getStatus() == PaymentStatus.PAID)
            throw new RuntimeException("Payment already processed on " + payroll.getDate());

        Employee employee = employeeRepository.getEmployeeById(payroll.getEmployee())
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with id: " + payroll.getEmployee() + " Not Found!"));

        EmployeeCTC employeeCTC = ctcRepository.getCtcByEmployee(payroll.getEmployee())
                .orElseThrow(() -> new EmployeeCtcNotFound("CTC for employee id: " + payroll.getEmployee() + " Not Found!"));

        Deductions deductions = deductionsRepo.getDeductionByEmployee(payroll.getEmployee())
                .orElseThrow(() -> new DeductionNotFound("Deduction for employee id: " + payroll.getEmployee() + " Not Found!"));

        BankAccountDetail bankAccountDetail = bankAccountDetailRepo.findByEmployeeId(payroll.getEmployee())
                .orElseThrow(() -> new BankAccountNotFoundException("Bank for user id: " + payroll.getEmployee() + " Not Found!"));

        payroll.setStatus(PaymentStatus.PAID);
        payroll.setDate(LocalDate.now());
        if (!testEmail.isEmpty())
            payroll.setTestEmail(testEmail);

        try {
            Payroll savedPayroll = payrollRepo.save(payroll);
            mailService.sendPayrollEmail(
                    EmailDataDto.builder()
                            .month(payroll.getMonthYear().split("-")[0])
                            .year(payroll.getMonthYear().split("-")[1])
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
                            .bankName(bankAccountDetail.getBankName())
                            .accountNumber(bankAccountDetail.getBankAccountNumber())
                            .IFSCCode(bankAccountDetail.getIfscCode())
                            .transactionID(txnNum)
                            .creditDateTime(String.valueOf(LocalDate.now()))
                            .build(),
                    testEmail
            );

            return savedPayroll;
        } catch (Exception e) {
            throw new RuntimeException("Failed to process the payroll!");
        }
    }
}
