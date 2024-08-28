package org.event.event.model;

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
public class EventRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull(message = "Event cannot be null")
    private Event event;

    @NotNull(message = "Athlete ID cannot be null")
    @Column(nullable = false)
    private UUID athleteId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime registrationDate;

    public enum Status {
        PENDING,
        APPROVED,
        REJECTED
    }
}
