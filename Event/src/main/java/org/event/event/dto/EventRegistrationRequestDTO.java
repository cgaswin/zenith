package org.event.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRegistrationRequestDTO {
    private String eventId;
    private String eventItemId;
    private String athleteId;
}
