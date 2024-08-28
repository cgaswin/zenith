package org.event.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.event.event.model.Event;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventItemResponseDTO {
    private UUID id;
    private UUID eventId;
    private String name;
    private String description;
}
