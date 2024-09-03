package org.event.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventResponseDTO {
    private String id;
    private String name;
    private String description;
    private String photoUrl;
    private String venue;
    private String date;
}
