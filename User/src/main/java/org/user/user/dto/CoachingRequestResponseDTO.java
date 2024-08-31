package org.user.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.user.user.model.Athlete;
import org.user.user.model.Coach;
import org.user.user.model.CoachingRequest.Status;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoachingRequestResponseDTO {
    private String id;
    private Athlete athlete;
    private Coach coach;
    private Status status;
}
