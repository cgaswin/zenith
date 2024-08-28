package org.event.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRegistrationResponseDTO {
    private UUID id;
    private UUID eventId;
    private UUID eventItemId;
    private UUID athleteId;
    private Status status;
    private LocalDateTime registrationDate;

    public enum Status {
        PENDING,
        APPROVED,
        REJECTED
    }
}
