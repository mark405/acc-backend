package com.traffgun.acc.mapper;

import com.traffgun.acc.dto.ticket.TicketCommentResponse;
import com.traffgun.acc.entity.TicketComment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketCommentMapper {
    private final UserMapper userMapper;
    private final TicketFileMapper ticketFileMapper;

    public TicketCommentResponse toDto(TicketComment ticketComment) {
        return new TicketCommentResponse(
                ticketComment.getId(),
                ticketComment.getText(),
                ticketComment.getAttachments().stream().map(ticketFileMapper::toDto).toList(),
                userMapper.toUserDto(ticketComment.getCreatedBy()),
                ticketComment.getCreatedAt()
        );
    }
}
