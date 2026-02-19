package com.traffgun.acc.dto.ticket;

import com.traffgun.acc.model.TicketStatus;
import com.traffgun.acc.model.TicketType;
import lombok.Data;

import java.util.List;

@Data
public class TicketFilter {
    private List<TicketType> types;
    private TicketStatus status;
    private Long createdBy;
    private Long assignedTo;
    private String sortBy = "id";
    private String direction = "desc";
    private int page = 0;
    private int size = 25;
}
