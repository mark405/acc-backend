package com.traffgun.acc.entity;

import com.traffgun.acc.model.TicketStatus;
import com.traffgun.acc.model.TicketType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@NamedEntityGraph(
        name = "Ticket.full",
        attributeNodes = {
                @NamedAttributeNode("files"),
                @NamedAttributeNode("createdBy"),
                @NamedAttributeNode("assignedTo")
        }
)
@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status = TicketStatus.OPENED;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketFile> files = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "assigned_to", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> assignedTo = new ArrayList<>();

    @Column(nullable = false)
    private Instant createdAt = Instant.now();
}
