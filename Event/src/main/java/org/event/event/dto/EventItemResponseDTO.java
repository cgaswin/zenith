package org.event.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventItemResponseDTO {
    private String id;
    private String eventId;
    private String name;
    private String description;
}
