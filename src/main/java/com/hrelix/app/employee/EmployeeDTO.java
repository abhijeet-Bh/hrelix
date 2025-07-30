package com.hrelix.app.employee;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDTO {

    // Getters and Setters
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Double salary;
    private LocalDate joiningDate;
    private String avatar;
    private String team;
    private String position;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Password will only be accepted in requests
    private String password;

    private List<Role> roles;

}