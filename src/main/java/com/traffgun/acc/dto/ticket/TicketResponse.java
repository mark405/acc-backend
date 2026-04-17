package com.traffgun.acc.dto.ticket;

import com.traffgun.acc.dto.employee.EmployeeResponse;
import com.traffgun.acc.model.TicketStatus;
import com.traffgun.acc.model.TicketType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
public class TicketResponse {
    private Long id;
    private String text;
    private TicketType type;
    private TicketStatus status;
    private List<EmployeeResponse> assignedTo;
    private List<TicketFileResponse> files;
    private EmployeeResponse operatedBy;
    private EmployeeResponse createdBy;
    private Instant createdAt;
    private Boolean isArchived;
}
