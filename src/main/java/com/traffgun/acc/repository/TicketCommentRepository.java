package com.traffgun.acc.repository;

import com.traffgun.acc.entity.Ticket;
import com.traffgun.acc.entity.TicketComment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketCommentRepository extends JpaRepository<TicketComment, Long> {
    void deleteAllByTicket(Ticket ticket);

    @EntityGraph(value = "TicketComment.attachmentsAndCreator")
    List<TicketComment> findAllByTicketIdOrderByIdDesc(Long ticketId);

    List<TicketComment> findAllByTicket(Ticket ticket);
}
