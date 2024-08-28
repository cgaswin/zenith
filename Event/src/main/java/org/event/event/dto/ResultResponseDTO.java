package org.event.event.dto;

import lombok.Data;
import org.event.event.model.Event;
import org.event.event.model.EventItem;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ResultResponseDTO {
    private UUID id;
    private Event event;
    private EventItem eventItem;
    private UUID athleteId;
    private BigDecimal score;
}
