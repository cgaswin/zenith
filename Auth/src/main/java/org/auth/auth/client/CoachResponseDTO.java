package org.auth.auth.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoachResponseDTO {
    private String id;
    private String name;
    private String dob;
    private String gender;
    private String category;
    private String description;
    private String photoUrl;
    // Coach-specific fields
    private List<String> achievements;
}
