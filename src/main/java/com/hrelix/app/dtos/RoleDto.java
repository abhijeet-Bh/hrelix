package com.hrelix.app.dtos;

import com.hrelix.app.models.Role;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Hidden
public class RoleDto {
    private List<Role> roles;
}
