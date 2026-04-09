package com.traffgun.acc.dto.project;

import com.traffgun.acc.dto.user.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProjectResponse {
    private Long id;
    private String name;
    private UserResponse createdBy;
    private String comment;
    private Integer index;
}
