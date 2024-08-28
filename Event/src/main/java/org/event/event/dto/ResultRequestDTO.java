package org.event.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultRequestDTO {
    private UUID eventId;
    private UUID eventItemId;
    private UUID athleteId;
    private BigDecimal score;
}
