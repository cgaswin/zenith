package org.auth.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDTO {
        private String name;
        private String username;
        private String email;
        private String password;
        private String role;
        private MultipartFile image;

        // Additional fields for Athlete
        private Date dob;
        private String gender;
        private String height;
        private String weight;
        private String category;
        private String description;
        private String photoUrl;

        // Coach-specific fields
        private List<String> achievements;


}
