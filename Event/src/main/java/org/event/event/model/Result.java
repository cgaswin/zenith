package org.event.event.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.event.event.commons.utils.StringPrefixedSequenceIdGenerator;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "res_seq")
    @GenericGenerator(
            name = "res_seq",
            strategy = "org.event.event.commons.utils.StringPrefixedSequenceIdGenerator",
            parameters = {
                    @Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "50"),
                    @Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "res_"),
                    @Parameter(name = StringPrefixedSequenceIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d") })
    private String id;

    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull(message = "Event cannot be null")
    private Event event;

    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull(message = "Event item cannot be null")
    private EventItem eventItem;

    @NotNull(message = "Athlete ID cannot be null")
    @Column(nullable = false)
    private String athleteId;

    @Column(nullable = false, precision = 10, scale = 2)
    @NotNull(message = "Score cannot be null")
    private BigDecimal score;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime publishedDate;
}

