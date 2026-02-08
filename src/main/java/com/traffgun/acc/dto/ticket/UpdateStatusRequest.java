package com.traffgun.acc.dto.ticket;

import com.traffgun.acc.model.TicketStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateStatusRequest {
    @NotNull
    private TicketStatus status;
}
