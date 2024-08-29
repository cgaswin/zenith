package org.user.user.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coach {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "User cannot be empty")
    private UUID user_id;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    private String description;

    private String photoUrl;

    private boolean is_accepting_requests=true;

    private String achievements;

    private List<String> getAchievements(){
        return this.achievements!=null ? Arrays.asList(achievements.split(",")):null;
    }

    private void setAchievements(List<String> achievements){
        this.achievements = achievements!=null?String.join(",",achievements):null;
    }


}
