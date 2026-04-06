package com.traffgun.acc.entity;

import com.traffgun.acc.model.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank
    private String username;

    @Column(nullable = false)
    @NotBlank
    private String password;

    @Column(nullable = false, columnDefinition = "varchar")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "modified_at", nullable = false)
    private Instant modifiedAt;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(name = "totp_secret")
    private String totpSecret;

    @Column(name = "totp_enabled")
    @Builder.Default
    private Boolean totpEnabled = false;

    public boolean getTotpEnabled() {
        return totpEnabled != null && totpEnabled;
    }

    @Column(name = "offers_editable")
    @Builder.Default
    private Boolean offersEditable = false;
}
