package com.hrelix.app.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hrelix.app.models.utils.Role;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class EmployeeDTO {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Double salary;
    private LocalDate joiningDate;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Password will only be accepted in requests
    private String password;

    private List<Role> roles;

    // Constructors
    public EmployeeDTO() {}

    public EmployeeDTO(UUID id, String firstName, String lastName, String email, String phone, Double salary, LocalDate joiningDate, String password, List<Role> roles) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.salary = salary;
        this.joiningDate = joiningDate;
        this.password = password;
        this.roles = roles;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}