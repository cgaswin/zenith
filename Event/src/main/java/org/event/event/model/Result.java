package org.event.event.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull(message = "Event cannot be null")
    private Event event;

    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull(message = "Event item cannot be null")
    private EventItem eventItem;

    @NotNull(message = "Athlete ID cannot be null")
    @Column(nullable = false)
    private UUID athleteId;

    @Column(nullable = false, precision = 5, scale = 2)
    @NotNull(message = "Score cannot be null")
    private BigDecimal score;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime publishedDate;
}

