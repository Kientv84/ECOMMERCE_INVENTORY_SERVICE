package com.ecommerce.inventory.dtos.KafkaTemplate;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventMetadata {
    private UUID eventId;
    private String eventType;
    private String source;
    private int version;
}

