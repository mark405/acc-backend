package com.traffgun.acc.dto.ticket;

import com.traffgun.acc.model.TicketStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import jakarta.validation.constraints.NotEmpty;

@Data
public class UpdateTicketRequest {
    @NotNull
    private String text;
    @NotNull
    private TicketStatus status;
    @NotEmpty
    private List<Long> assignedTo;
    private List<MultipartFile> filesToAdd;
    private List<Long> filesToDelete;
}
