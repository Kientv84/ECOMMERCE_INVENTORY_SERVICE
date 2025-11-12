package com.ecommerce.inventory.dtos.responses.kafka;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KafkaEventInventory {
    private UUID orderId;
    private List<KafkaShipmentItemResponse> items;
}

