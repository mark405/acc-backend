package com.traffgun.acc.entity;

import com.traffgun.acc.model.EmployeeRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "telegram_users ")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TelegramUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long chatId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EmployeeRole role;

    @Column
    private Long managerId;
}
