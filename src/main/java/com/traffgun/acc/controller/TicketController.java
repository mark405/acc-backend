package com.traffgun.acc.controller;

import com.traffgun.acc.dto.CreateCommentRequest;
import com.traffgun.acc.dto.UpdateCommentRequest;
import com.traffgun.acc.dto.ticket.*;
import com.traffgun.acc.entity.Ticket;
import com.traffgun.acc.exception.EntityNotFoundException;
import com.traffgun.acc.mapper.TicketCommentMapper;
import com.traffgun.acc.mapper.TicketMapper;
import com.traffgun.acc.model.TicketStatus;
import com.traffgun.acc.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;
    private final TicketCommentMapper ticketCommentMapper;

    @PostMapping
    public Page<TicketResponse> getAllTickets(@Valid @RequestBody TicketFilter filter) {
        return ticketService.findAll(filter).map(ticketMapper::toDto);
    }

    @GetMapping("/{id}/comments")
    public List<TicketCommentResponse> getCommentsByTicketId(@PathVariable("id") Long id) {
        return ticketService.findAllComments(id).stream().map(ticketCommentMapper::toDto).toList();
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('MANAGER') || hasRole('ADMIN')")
    public TicketResponse createTicket(@ModelAttribute @Valid CreateTicketRequest request) throws IllegalAccessException {
        Ticket createdTicket = ticketService.create(request);
        return ticketMapper.toDto(createdTicket);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('MANAGER') || hasRole('ADMIN')")
    public TicketResponse updateTicket(@PathVariable("id") Long id, @ModelAttribute @Valid UpdateTicketRequest request) {
        Ticket ticket = ticketService.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        Ticket createdTicket = ticketService.update(ticket, request);
        return ticketMapper.toDto(createdTicket);
    }

    @PutMapping("/status/{id}")
    @PreAuthorize("hasRole('TECH_MANAGER') || hasRole('OFFERS_MANAGER')")
    public ResponseEntity<Void> changeStatus(@PathVariable("id") Long id, @RequestBody UpdateStatusRequest status) {
        ticketService.changeStatus(id, status.getStatus());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteTicket(@PathVariable("id") Long id) {
        ticketService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/comments")
    public TicketCommentResponse addComment(@PathVariable Long id, @ModelAttribute CreateCommentRequest request) throws IllegalAccessException {
        return ticketCommentMapper.toDto(ticketService.addComment(id, request));
    }

    @PutMapping("/comments/{id}")
    public TicketCommentResponse editComment(@PathVariable Long id, @ModelAttribute UpdateCommentRequest request) {
        return ticketCommentMapper.toDto(ticketService.editComment(id, request));
    }

    @DeleteMapping("/comments/{id}")
    public void deleteComment(@PathVariable Long id) {
        ticketService.deleteComment(id);
    }
}