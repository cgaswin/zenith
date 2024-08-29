package org.user.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoachRequestDTO {
    @NotNull(message = "userId is null")
    private UUID userId;
    private String name;
    private String description;
    private String photoUrl;
    private List<String> achievements;

}
