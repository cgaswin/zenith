package org.user.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoachResponseDTO {
    private UUID id;
    private String name;
    private String description;
    private String photoUrl;
    private List<String> achievements;
    private boolean isAcceptingRequests;
}
