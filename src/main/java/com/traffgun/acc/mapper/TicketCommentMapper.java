package com.traffgun.acc.mapper;

import com.traffgun.acc.dto.ticket.TicketCommentResponse;
import com.traffgun.acc.entity.TicketComment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class TicketCommentMapper {
    private final EmployeeMapper employeeMapper;
    private final TicketFileMapper ticketFileMapper;

    public TicketCommentResponse toDto(TicketComment ticketComment) {
        return new TicketCommentResponse(
                ticketComment.getId(),
                ticketComment.getText(),
                ticketComment.getAttachments() != null ? ticketComment.getAttachments().stream().map(ticketFileMapper::toDto).toList() : new ArrayList<>(),
                employeeMapper.toDto(ticketComment.getCreatedBy()),
                ticketComment.getCreatedAt()
        );
    }
}
