package com.hrelix.app.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hrelix.app.models.utils.Role;
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

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Password will only be accepted in requests
    private String password;

    private List<Role> roles;

}