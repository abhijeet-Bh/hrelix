package com.hrelix.app.models;

import com.hrelix.app.models.utils.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employees", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"), // Ensures that email is unique
        @UniqueConstraint(columnNames = "phone")  // Ensures that phone is unique
})
public class Employee {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "first_name", nullable = false) // Database constraint: Not null
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true) // Email must be unique and not null
    private String email;

    @Column(nullable = false, unique = true) // Phone number must be unique and not null
    private String phone;

    @Column(nullable = false)
    private Double salary;

    @Column(nullable = false)
    private LocalDate joiningDate;


    // Setter for password
    // Getter for password
    private String password; // New password field

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Role> roles;

    // Getters and Setters
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());
    }

}
