package org.event.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventResponseDTO {
    private String name;
    private String description;
    private String photoUrl;
    private String venue;
    private LocalDateTime date;
}
