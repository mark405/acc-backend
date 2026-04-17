package com.traffgun.acc.dto.ticket;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateArchiveRequest {
    @NotNull
    private Boolean archive;
}
