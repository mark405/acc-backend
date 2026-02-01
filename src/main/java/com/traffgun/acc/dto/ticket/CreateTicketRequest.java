package com.traffgun.acc.dto.ticket;

import com.traffgun.acc.model.TicketType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CreateTicketRequest {
    @NotNull
    private String text;
    @NotNull
    private TicketType type;
    @NotEmpty
    private List<Long> assignedTo;
    private List<MultipartFile> files;
}
