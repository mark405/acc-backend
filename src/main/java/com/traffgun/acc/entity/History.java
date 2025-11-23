package com.traffgun.acc.entity;

import com.traffgun.acc.converters.HistoryBodyConverter;
import com.traffgun.acc.model.history.HistoryBody;
import com.traffgun.acc.model.history.HistoryType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "histories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "date", nullable = false, updatable = false)
    private Instant date;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private HistoryType type;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @Convert(converter = HistoryBodyConverter.class)
    @NotNull
    private HistoryBody body;
}
