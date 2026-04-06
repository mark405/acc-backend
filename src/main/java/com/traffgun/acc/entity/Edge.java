package com.traffgun.acc.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "edges")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Edge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long projectId;

    private Long source;

    private Long target;
}
