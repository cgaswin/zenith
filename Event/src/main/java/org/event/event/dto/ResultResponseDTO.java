package org.event.event.dto;

import lombok.Data;
import org.event.event.model.Event;
import org.event.event.model.EventItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ResultResponseDTO {
    private String id;
    private String eventId;
    private String eventName;
    private String eventItemId;
    private String eventItemName;
    private String athleteId;
    private String athleteName;
    private BigDecimal score;
    private LocalDateTime publishedDate;
}
