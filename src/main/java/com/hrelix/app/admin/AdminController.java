package com.hrelix.app.admin;

import com.hrelix.app.employee.EmployeeDTO;
import com.hrelix.app.employee.RoleDto;
import com.hrelix.app.utilities.ApiResponse;
import com.hrelix.app.utilities.ErrorResponse;
import com.hrelix.app.utilities.SuccessResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
            return new ResponseEntity<>(new SuccessResponse<>(true, 201, emp), HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new ErrorResponse(503, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create-new-employee-with-role")
    public ResponseEntity<ApiResponse> createEmployee(@RequestBody EmployeeDTO employee) {
        try {
            EmployeeDTO emp = adminService.createEmployee(employee);
            return new ResponseEntity<>(new SuccessResponse<>(true, 201, emp), HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new ErrorResponse(503, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete-employee/{id}")
    public ResponseEntity<ApiResponse> deleteEmployee(@PathVariable String id) {
        try {
            adminService.deleteEmployee(id);
            return new ResponseEntity<>(new SuccessResponse<>(true, 200, "Employee Deleted Successfully!"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(404, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update-role/{id}")
    public ResponseEntity<ApiResponse> updateEmployee(@RequestBody RoleDto roles, @PathVariable String id) {
        try {
            EmployeeDTO emp = adminService.updateRole(roles, id);
            return new ResponseEntity<>(new SuccessResponse<>(true, 201, emp), HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new ErrorResponse(503, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
