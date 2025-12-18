package com.ecommerce.inventory.messaging.consumers;

import com.ecommerce.inventory.dtos.KafkaTemplate.KafkaEvent;
import com.ecommerce.inventory.dtos.responses.kafka.KafkaEventInventory;
import com.ecommerce.inventory.services.ProductInventoryService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class ShippingShippedConsumer {
    private final ProductInventoryService productInventoryService;

    @KafkaListener(
            topics = "${spring.kafka.shipping.topic.shipping-shipped}",
            groupId = "spring.kafka.shipping.group",
            containerFactory = "kafkaListenerContainerFactory")
    public void onMessageHandler(@Payload String message) {
        try {
            log.info("[ShippingShippedConsumer] Start consuming message ...");
            log.info("[ShippingShippedConsumer] Received message payload: {}", message);

            ObjectMapper objectMapper = new ObjectMapper();

            KafkaEvent<KafkaEventInventory> event =
                    objectMapper.readValue(
                            message,
                            new TypeReference<KafkaEvent<KafkaEventInventory>>() {}
                    );

            KafkaEventInventory payload = event.getPayload();


            productInventoryService.deductInventory(payload);
            log.info("[ShippingShippedConsumer] Process inventory deduct ...");
        } catch (Exception e) {
            log.error("[onMessageHandler] Error while inventory deduct . Err {}", e.getMessage());
        }
    }
}
