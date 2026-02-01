package com.traffgun.acc.repository;

import com.traffgun.acc.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {
    @NonNull
    @EntityGraph(value = "Ticket.full")
    Page<Ticket> findAll(@Nullable Specification<Ticket> spec, @NonNull Pageable pageable);

    @NonNull
    @EntityGraph(value = "Ticket.full")
    Optional<Ticket> findById(@NonNull Long id);

}
