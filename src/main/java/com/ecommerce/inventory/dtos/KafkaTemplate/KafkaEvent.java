package com.ecommerce.inventory.dtos.KafkaTemplate;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KafkaEvent<T> {
    private EventMetadata metadata;
    private T payload;
}

