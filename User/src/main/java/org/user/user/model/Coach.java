package org.user.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "UserId"))
public class Coach {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "User cannot be empty")
    private UUID user_id;


    private String description;

    private String photoUrl;

    private boolean isAcceptingRequests=true;

    private String achievements;

    public List<String> getAchievements(){
        return this.achievements!=null ? Arrays.asList(achievements.split(",")):null;
    }

    public void setAchievements(List<String> achievements){
        this.achievements = achievements!=null?String.join(",",achievements):null;
    }


}
