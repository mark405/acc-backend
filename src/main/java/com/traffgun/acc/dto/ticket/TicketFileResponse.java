package com.traffgun.acc.dto.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TicketFileResponse {
    private Long id;
    private String fileName;
    private String fileUrl;
}
