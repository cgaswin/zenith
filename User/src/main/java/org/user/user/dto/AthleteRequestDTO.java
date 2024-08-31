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
    private String userId;
    private String name;
    private String gender;
    private String height;
    private String weight;
    private String category;
    private String description;
    private String photoUrl;
}
