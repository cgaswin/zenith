package org.user.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AthleteResponseDTO {
    private UUID id;
    private String name;
    private String description;
    private String photoUrl;
}
