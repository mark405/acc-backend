package com.traffgun.acc.dto.ticket;

import com.traffgun.acc.dto.user.UserResponse;
import com.traffgun.acc.model.TicketStatus;
import com.traffgun.acc.model.TicketType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
public class TicketCommentResponse {
    private Long id;
    private String text;
    private List<TicketFileResponse> attachments;
    private UserResponse createdBy;
    private Instant createdAt;
}
