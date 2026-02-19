package com.traffgun.acc.service;

import com.traffgun.acc.dto.CreateCommentRequest;
import com.traffgun.acc.dto.UpdateCommentRequest;
import com.traffgun.acc.dto.ticket.CreateTicketRequest;
import com.traffgun.acc.dto.ticket.TicketFilter;
import com.traffgun.acc.dto.ticket.UpdateTicketRequest;
import com.traffgun.acc.entity.Ticket;
import com.traffgun.acc.entity.TicketComment;
import com.traffgun.acc.entity.TicketFile;
import com.traffgun.acc.entity.User;
import com.traffgun.acc.exception.EntityNotFoundException;
import com.traffgun.acc.model.Role;
import com.traffgun.acc.model.TicketStatus;
import com.traffgun.acc.model.TicketType;
import com.traffgun.acc.repository.TicketCommentRepository;
import com.traffgun.acc.repository.TicketFileRepository;
import com.traffgun.acc.repository.TicketRepository;
import com.traffgun.acc.specification.OperationSpecification;
import com.traffgun.acc.specification.TicketSpecification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository repository;
    private final TicketFileRepository ticketFileRepository;
    private final TicketCommentRepository ticketCommentRepository;
    private final UserService userService;

    @Transactional
    public Ticket create(@Valid CreateTicketRequest request) throws IllegalAccessException {
        List<User> users = userService.findAllByIds(request.getAssignedTo());

        Ticket ticket = Ticket.builder()
                .text(request.getText())
                .type(request.getType())
                .status(TicketStatus.OPENED)
                .createdBy(userService.getCurrentUser())
                .assignedTo(new HashSet<>(users))
                .build();

        if (request.getFiles() != null && !request.getFiles().isEmpty()) {
            addFiles(request.getFiles(), ticket);
        }

        return repository.save(ticket);
    }

    private void addFiles(List<MultipartFile> filesToAdd, Ticket ticket) {
        Path uploadDir = Paths.get("ticket_files");
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }

        for (MultipartFile file : filesToAdd) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path targetPath = uploadDir.resolve(fileName);

            try {
                Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save file: " + fileName, e);
            }

            // Create TicketFile entity
            TicketFile ticketFile = TicketFile.builder()
                    .fileName(file.getOriginalFilename())
                    .fileUrl("ticket-files/" + fileName)
                    .ticket(ticket)
                    .build();

            if (ticket.getFiles() == null) {
                ticket.setFiles(new HashSet<>());
            }
            ticket.getFiles().add(ticketFile);
        }
    }

    @Transactional
    public Ticket update(Ticket ticket, @Valid UpdateTicketRequest request) {
        ticket.setText(request.getText());
        ticket.setStatus(request.getStatus());
        List<User> users = userService.findAllByIds(request.getAssignedTo());
        ticket.setAssignedTo(new HashSet<>(users));
        if (request.getFilesToDelete() != null) {
            removeFiles(ticket, request.getFilesToDelete());
        }
        if (request.getFilesToAdd() != null) {
            addFiles(request.getFilesToAdd(), ticket);
        }

        return repository.save(ticket);
    }

    private void removeFiles(Ticket ticket, List<Long> filesToDelete) {
        List<TicketFile> remainingFiles = new ArrayList<>();

        for (TicketFile file : ticket.getFiles()) {
            if (filesToDelete.contains(file.getId())) {
                try {
                    Files.deleteIfExists(Paths.get(file.getFileUrl()));
                } catch (IOException e) {
                    System.err.println("Failed to delete file: " + file.getFileUrl());
                }
            } else {
                remainingFiles.add(file);
            }
        }

        ticket.getFiles().clear();
        ticket.getFiles().addAll(remainingFiles);
    }

    @Transactional(readOnly = true)
    public Optional<Ticket> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public void deleteById(Long id) {
        Ticket ticket = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        ticket.getFiles().forEach(f -> {
            try { Files.deleteIfExists(Paths.get(f.getFileUrl())); } catch (Exception ignored) {}
        });
        ticketCommentRepository.findAllByTicket(ticket).forEach(comment -> {
            comment.getAttachments().forEach(f -> {
                try { Files.deleteIfExists(Paths.get(f.getFileUrl())); } catch (Exception ignored) {}
            });
        });
        ticketCommentRepository.deleteAllByTicket(ticket);

        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<Ticket> findAll(@Valid TicketFilter filter) {
        Specification<Ticket> spec = (root, query, cb) -> cb.conjunction();

        spec = spec
                .and(TicketSpecification.hasTypes(filter.getTypes()))
                .and(TicketSpecification.hasStatus(filter.getStatus()))
                .and(TicketSpecification.hasCreatedBy(filter.getCreatedBy()))
                .and(TicketSpecification.hasAssignedTo(filter.getAssignedTo()));

        Sort sort = Sort.by(Sort.Direction.fromString(filter.getDirection()), filter.getSortBy());
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(), sort);

        return repository.findAll(spec, pageable);
    }

    @Transactional
    public TicketComment addComment(Long ticketId, CreateCommentRequest request) throws IllegalAccessException {
        Ticket ticket = repository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        TicketComment comment = TicketComment.builder()
                .text(request.getText())
                .ticket(ticket)
                .createdBy(userService.getCurrentUser())
                .build();

        // handle attachments
        if (request.getAttachments() != null && !request.getAttachments().isEmpty()) {
            Path uploadDir = Paths.get("ticket_files/comments");
            try { Files.createDirectories(uploadDir); } catch (Exception e) { throw new RuntimeException(e); }

            List<TicketFile> files = new ArrayList<>();
            for (var file : request.getAttachments()) {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path targetPath = uploadDir.resolve(fileName);
                try { Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING); }
                catch (Exception e) { throw new RuntimeException("Failed to save file", e); }

                files.add(TicketFile.builder()
                        .fileName(file.getOriginalFilename())
                        .fileUrl("ticket-files/comments/" + fileName)
                        .comment(comment)
                        .build());
            }
            comment.setAttachments(files);
        }

        var saved = ticketCommentRepository.save(comment);

        if (ticket.getType() == TicketType.TECH_GOAL
                && ticket.getStatus() == TicketStatus.CLOSED
                && userService.getCurrentUser().getRole() == Role.MANAGER) {
            ticket.setStatus(TicketStatus.OPENED);
            repository.save(ticket);
        }

        return saved;
    }

    @Transactional
    public TicketComment editComment(Long commentId, UpdateCommentRequest request) {
        TicketComment comment = ticketCommentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (request.getText() != null) comment.setText(request.getText());

        // delete attachments
        if (request.getAttachmentsToDelete() != null) {
            var iterator = comment.getAttachments().iterator();
            while (iterator.hasNext()) {
                TicketFile file = iterator.next();
                if (request.getAttachmentsToDelete().contains(file.getId())) {
                    try { Files.deleteIfExists(Paths.get(file.getFileUrl())); } catch (Exception ignored) {}
                    iterator.remove();
                }
            }
        }

        // add new attachments
        if (request.getAttachmentsToAdd() != null && !request.getAttachmentsToAdd().isEmpty()) {
            Path uploadDir = Paths.get("ticket_files");
            try { Files.createDirectories(uploadDir); } catch (Exception e) { throw new RuntimeException(e); }

            for (var file : request.getAttachmentsToAdd()) {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path targetPath = uploadDir.resolve(fileName);
                try { Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING); }
                catch (Exception e) { throw new RuntimeException("Failed to save file", e); }

                comment.getAttachments().add(TicketFile.builder()
                        .fileName(file.getOriginalFilename())
                        .fileUrl(targetPath.toString())
                        .comment(comment)
                        .build());
            }
        }

        return ticketCommentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        TicketComment comment = ticketCommentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException(commentId));

        comment.getAttachments().forEach(f -> {
            try { Files.deleteIfExists(Paths.get(f.getFileUrl())); } catch (Exception ignored) {}
        });

        ticketCommentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public List<TicketComment> findAllComments(Long id) {
        return ticketCommentRepository.findAllByTicketIdOrderByIdDesc(id);
    }

    @Transactional
    public void changeStatus(Long id, TicketStatus status) throws IllegalAccessException {
        Ticket ticket = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        ticket.setStatus(status);
        if (ticket.getType() == TicketType.TECH_GOAL) {
            if (status == TicketStatus.IN_PROGRESS) {
                ticket.setOperatedBy(userService.getCurrentUser());
            } else {
                ticket.setOperatedBy(null);
            }
        }
        repository.save(ticket);
    }
}
