package com.hrelix.app.admin;

import com.hrelix.app.employee.EmployeeDTO;
import com.hrelix.app.employee.RoleDto;
import com.hrelix.app.utilities.ApiResponse;
import com.hrelix.app.utilities.SuccessResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@Tag(name = "Admin Endpoints", description = "Operations related to Employee for Admin")
public class AdminController {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private AdminService adminService;

    @PostMapping("/register")
    @Hidden
    public ResponseEntity<ApiResponse> registerAdmin(@RequestBody EmployeeDTO adminDTO) {
        try {
            EmployeeDTO emp = adminService.createAdmin(adminDTO);
            return ResponseEntity.status(201).body(
                    new SuccessResponse<>(
                            "Admin Created Successfully!",
                            emp
                    ));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(503).body(
                    new SuccessResponse<>(
                            "failed to create Admin!",
                            e.getMessage()
                    ));
        }
    }

    @PostMapping("/create-new-employee-with-role")
    public ResponseEntity<ApiResponse> createEmployee(@RequestBody EmployeeDTO employee) {
        try {
            EmployeeDTO emp = adminService.createEmployee(employee);
            return ResponseEntity.status(201).body(
                    new SuccessResponse<>(
                            "New Employee created Successfully!",
                            emp
                    ));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.ok(
                    new SuccessResponse<>(
                            "Failed to create Employee!",
                            e.getMessage()
                    ));
        }
    }

    @DeleteMapping("/delete-employee/{id}")
    public ResponseEntity<ApiResponse> deleteEmployee(@PathVariable String id) {
        try {
            adminService.deleteEmployee(id);
            return ResponseEntity.ok(
                    new SuccessResponse<>(
                            "Employee deleted Successfully!",
                            null
                    ));
        } catch (Exception e) {
            return ResponseEntity.ok(
                    new SuccessResponse<>(
                            "Failed to delete employee!",
                            e.getMessage()
                    ));
        }
    }

    @PutMapping("/update-role/{id}")
    public ResponseEntity<ApiResponse> updateEmployee(@RequestBody RoleDto roles, @PathVariable String id) {
        try {
            EmployeeDTO emp = adminService.updateRole(roles, id);
            return ResponseEntity.status(201).body(
                    new SuccessResponse<>(
                            "Updated Employee Successfully!",
                            emp
                    ));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.ok(
                    new SuccessResponse<>(
                            "Failed to update role!",
                            e.getMessage()
                    ));
        }
    }
}
