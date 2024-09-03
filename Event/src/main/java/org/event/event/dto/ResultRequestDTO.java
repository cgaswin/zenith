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
    private String eventId;
    private String eventItemId;
    private String athleteId;
    private BigDecimal score;
}
