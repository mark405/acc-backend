package com.traffgun.acc.mapper;

import com.traffgun.acc.dto.ticket.TicketResponse;
import com.traffgun.acc.entity.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class TicketMapper {
    private final UserMapper userMapper;
    private final TicketFileMapper ticketFileMapper;

    public TicketResponse toDto(Ticket ticket) {
        return new TicketResponse(
                ticket.getId(),
                ticket.getText(),
                ticket.getType(),
                ticket.getStatus(),
                ticket.getAssignedTo().stream().map(userMapper::toUserDto).toList(),
                ticket.getFiles() != null ? ticket.getFiles().stream().map(ticketFileMapper::toDto).toList() : new ArrayList<>(),
                userMapper.toUserDto(ticket.getCreatedBy()),
                userMapper.toUserDto(ticket.getOperatedBy()),
                ticket.getCreatedAt()
        );
    }
}
