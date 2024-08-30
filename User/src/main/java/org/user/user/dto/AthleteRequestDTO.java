package org.user.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AthleteRequestDTO {
    @NotNull(message = "userId is null")
    private UUID userId;
    private String description;
    private String photoUrl;
}
