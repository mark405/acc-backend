package com.traffgun.acc.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ticket_files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName; // имя файла

    @Column(nullable = false)
    private String fileUrl; // или путь/URL к файлу

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;
}
