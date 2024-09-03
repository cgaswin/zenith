package org.user.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoachRequestDTO {
    private String userId;
    private String name;
    private String gender;
    private String dob;
    private String category;
    private String description;
    private String photoUrl;
    private List<String> achievements;

}
