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
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true)
    private UUID id;

    @NotBlank(message = "Event name cannot be empty")
    private String name;

    @NotBlank(message = "Event description cannot be empty")
    private String description;

    @NotBlank(message = "Photo URL cannot be empty")
    private String photoUrl;

    @NotNull(message = "Event date cannot be null")
    private LocalDateTime date;

    @NotBlank(message = "Event venue cannot be empty")
    private String venue;

    @NotNull(message = "Creator ID cannot be null")
    private UUID createdBy;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
