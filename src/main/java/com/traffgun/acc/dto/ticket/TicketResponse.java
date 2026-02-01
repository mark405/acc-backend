package com.traffgun.acc.dto.ticket;

import com.traffgun.acc.dto.user.UserResponse;
import com.traffgun.acc.model.TicketStatus;
import com.traffgun.acc.model.TicketType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
public class TicketResponse {
    private Long id;
    private String text;
    private TicketType type;
    private TicketStatus status;
    private List<UserResponse> assignedTo;
    private List<TicketFileResponse> files;
    private UserResponse createdBy;
    private Instant createdAt;
}
