package org.user.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoachingRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "Athlete cannot be null")
    @ManyToOne
    @JoinColumn(nullable = false)
    private Athlete athlete;

    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull(message = "Coach cannot be null")
    private Coach coach;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @CreationTimestamp
    private LocalDateTime request_date;

    private LocalDateTime response_date;


    public enum Status{
        PENDING,
        APPROVED,
        REJECTED
    }
}
