package com.traffgun.acc.entity;

import com.traffgun.acc.model.EmployeeRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@NamedEntityGraph(
        name = "Employee.user",
        attributeNodes = @NamedAttributeNode("user")
)
@NamedEntityGraph(
        name = "Employee.full",
        attributeNodes = {
                @NamedAttributeNode("user"),
                @NamedAttributeNode(value = "columns", subgraph = "columns-subgraph")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "columns-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("values")
                        }
                )
        }
)
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private String name;

    private String comment;

    private Double rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_employee_user", foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE")
    )
    private User user;

    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Project project;

    @Column(name = "project_id", insertable = false, updatable = false)
    private Long projectId;

    @Column(columnDefinition = "varchar")
    @Enumerated(EnumType.STRING)
    private EmployeeRole role;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EmployeeColumn> columns = new HashSet<>();
}
