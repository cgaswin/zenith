package org.event.event.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true)
    private UUID id;

    @ManyToOne
    @JoinColumn( nullable = false)
    @NotNull(message = "Event cannot be null")
    private Event event;

    @NotBlank(message = "Item name cannot be empty")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Item description cannot be empty")
    @Column(nullable = false)
    private String description;

    @NotNull(message = "Creator ID cannot be null")
    @Column(nullable = false)
    private UUID createdBy;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
