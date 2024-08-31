package org.user.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.user.user.commons.utils.StringPrefixedSequenceIdGenerator;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "UserId"))
public class Coach {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "res_seq")
    @GenericGenerator(
            name = "res_seq",
            strategy = "org.user.user.commons.utils.StringPrefixedSequenceIdGenerator",
            parameters = {
                    @Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "50"),
                    @Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "ch_"),
                    @Parameter(name = StringPrefixedSequenceIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d") })
    private String id;

    @NotNull(message = "User cannot be empty")
    private String userId;
    private String name;
    private String gender;
    private Date dob;
    private String category;
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
