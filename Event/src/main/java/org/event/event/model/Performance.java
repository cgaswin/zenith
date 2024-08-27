package org.event.event.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Performance {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Event eventId;

    @ManyToOne
    @JoinColumn(nullable = false)
    private EventItem itemId;

    @Column(nullable = false)
    private UUID athleteId;

    @Column(nullable = false)
    private RESULT result;

    @Column(nullable = false)
    private LocalDate performanceDate;

    public enum RESULT {
        FIRST,
        SECOND,
        THIRD,
        NONE
    }

}
