package com.traffgun.acc.controller;

import com.traffgun.acc.dto.CreateCommentRequest;
import com.traffgun.acc.dto.UpdateCommentRequest;
import com.traffgun.acc.dto.ticket.CreateTicketRequest;
import com.traffgun.acc.dto.ticket.TicketCommentResponse;
import com.traffgun.acc.dto.ticket.TicketFilter;
import com.traffgun.acc.dto.ticket.TicketResponse;
import com.traffgun.acc.entity.Ticket;
import com.traffgun.acc.mapper.TicketCommentMapper;
import com.traffgun.acc.mapper.TicketMapper;
import com.traffgun.acc.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @GetMapping
    public List<TicketResponse> getAllTickets(@Valid @RequestBody TicketFilter filter) {
        return ticketService.findAll(filter).stream().map(ticketMapper::toDto).toList();
    }

    @GetMapping("/{id}/comments")
    public List<TicketCommentResponse> getCommentsByTicketId(@PathVariable("id") Long id) {
        return ticketService.findAllComments(id).stream().map(ticketCommentMapper::toDto).toList();
    }

    @PostMapping()
    @PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
    public TicketResponse createTicket(@RequestBody @Valid CreateTicketRequest request) throws IllegalAccessException {
        Ticket createdTicket = ticketService.create(request);
        return ticketMapper.toDto(createdTicket);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteTicket(@PathVariable("id") Long id) {
        ticketService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/comments")
    public TicketCommentResponse addComment(@PathVariable Long id, @ModelAttribute CreateCommentRequest request) {
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