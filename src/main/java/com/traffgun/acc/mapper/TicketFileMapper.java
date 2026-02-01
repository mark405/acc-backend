package com.traffgun.acc.mapper;

import com.traffgun.acc.dto.ticket.TicketFileResponse;
import com.traffgun.acc.entity.TicketFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketFileMapper {
    public TicketFileResponse toDto(TicketFile ticketFile) {
        return new TicketFileResponse(
                ticketFile.getId(),
                ticketFile.getFileName(),
                ticketFile.getFileUrl()
        );
    }
}
