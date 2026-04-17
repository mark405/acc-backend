package com.traffgun.acc.repository;

import com.traffgun.acc.entity.Ticket;
import com.traffgun.acc.model.TicketStatus;
import com.traffgun.acc.model.TicketType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {
    @NonNull
    @EntityGraph(value = "Ticket.full")
    Page<Ticket> findAll(@Nullable Specification<Ticket> spec, @NonNull Pageable pageable);

    @NonNull
    @EntityGraph(value = "Ticket.full")
    Optional<Ticket> findById(@NonNull Long id);

    @EntityGraph(value = "Ticket.full")
    List<Ticket> findAllByType(TicketType type);

    @EntityGraph(value = "Ticket.full")
    List<Ticket> findAllByTypeAndStatusIn(TicketType type, Collection<TicketStatus> statuses);

    @Modifying
    @Query("""
    UPDATE Ticket t
    SET t.isArchived = true
    WHERE t.closedAt IS NOT NULL
      AND t.closedAt <= :threshold
      AND t.isArchived = false
""")
    int archiveOldTickets(@Param("threshold") Instant threshold);
}
