package com.traffgun.acc.dto.graph;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class AddNodeRequest {
    @NotNull
    private String type;
    private String name;
    @NotNull
    private String role;
    @NotNull
    private Double x;
    @NotNull
    private Double y;
    @NotNull
    private String color;
}