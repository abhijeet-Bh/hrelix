package com.hrelix.app.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeDetailService implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository; // Assuming you have a UserRepository

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Employee> userOptional = employeeRepository.getEmployeeByEmail(email);

        if (userOptional.isPresent()) {
            Employee user = userOptional.get();
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    // You can modify the authorities based on your role setup
                    AuthorityUtils.createAuthorityList(user.getAuthorities().toString())
            );
        } else {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
    }
}