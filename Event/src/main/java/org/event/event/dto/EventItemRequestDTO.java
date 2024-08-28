package org.event.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventItemRequestDTO {
    private UUID eventId;
    private String name;
    private String description;
    private UUID createdBy;
}
