package com.traffgun.acc.entity;

import com.traffgun.acc.model.LevelType;
import com.traffgun.acc.model.OperationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "boards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LevelType levelType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OperationType operationType;
}
