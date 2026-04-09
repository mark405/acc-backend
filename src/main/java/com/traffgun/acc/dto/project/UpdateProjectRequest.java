package com.traffgun.acc.dto.project;

import lombok.Data;

@Data
public class UpdateProjectRequest {
    private String name;
    private String comment;
    private Integer index;
}
