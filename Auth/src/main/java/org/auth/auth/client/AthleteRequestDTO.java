package org.auth.auth.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AthleteRequestDTO {
    private String name;
    private String dob;
    private String gender;
    private String height;
    private String weight;
    private String category;
    private String description;
    private String photoUrl;
}
