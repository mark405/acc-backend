package com.traffgun.acc.dto.user;

import com.traffgun.acc.model.EmployeeRole;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeRoleRequest {
    @NotNull
    private EmployeeRole role;
}
