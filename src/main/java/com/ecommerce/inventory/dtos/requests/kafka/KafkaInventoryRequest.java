package com.ecommerce.inventory.dtos.requests.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KafkaInventoryRequest {
    private UUID productId;
    private String productName;
    private Integer stock;
}
