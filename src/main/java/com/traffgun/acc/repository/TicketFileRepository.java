package com.traffgun.acc.repository;

import com.traffgun.acc.entity.Ticket;
import com.traffgun.acc.entity.TicketFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketFileRepository extends JpaRepository<TicketFile, Long> {
    List<TicketFile> findAllByTicket_Id(Long ticketId);

    List<TicketFile> findAllByTicket(Ticket ticket);
}
