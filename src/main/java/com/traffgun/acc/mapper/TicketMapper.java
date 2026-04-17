package com.traffgun.acc.mapper;

import com.traffgun.acc.dto.ticket.TicketResponse;
import com.traffgun.acc.entity.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class TicketMapper {
    private final EmployeeMapper employeeMapper;
    private final TicketFileMapper ticketFileMapper;

    public TicketResponse toDto(Ticket ticket) {
        return new TicketResponse(
                ticket.getId(),
                ticket.getText(),
                ticket.getType(),
                ticket.getStatus(),
                ticket.getAssignedTo().stream().map(employeeMapper::toDto).toList(),
                ticket.getFiles() != null ? ticket.getFiles().stream().map(ticketFileMapper::toDto).toList() : new ArrayList<>(),
                ticket.getOperatedBy() == null ? null: employeeMapper.toDto(ticket.getOperatedBy()),
                employeeMapper.toDto(ticket.getCreatedBy()),
                ticket.getCreatedAt(),
                ticket.getIsArchived()
        );
    }
}
