package org.user.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AthleteResponseDTO {
    private String id;
    private String name;
    private String gender;
    private String height;
    private String weight;
    private String category;
    private String description;
    private String photoUrl;
}
