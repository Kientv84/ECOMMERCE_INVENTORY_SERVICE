package com.ecommerce.inventory.messaging.consumers;

import com.ecommerce.inventory.dtos.requests.kafka.KafkaInventoryRequest;
import com.ecommerce.inventory.dtos.responses.kafka.KafkaEventInventory;
import com.ecommerce.inventory.services.ProductInventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductCreatedConsumer {
    private final ProductInventoryService productInventoryService;

    @KafkaListener(
            topics = "${spring.kafka.product.topic.product-created-inventory}",
            groupId = "spring.kafka.product.group",
            containerFactory = "kafkaListenerContainerFactory")
    public void onMessageHandler(@Payload String message) {
        try {
            log.info("[ProductCreatedConsumer] Start consuming message ...");
            log.info("[ProductCreatedConsumer] Received message payload: {}", message);

            KafkaInventoryRequest response = new ObjectMapper().readValue(message, KafkaInventoryRequest.class);

            productInventoryService.create(response);
            log.info("[ProductCreatedConsumer] Process inventory created ...");
        } catch (Exception e) {
            log.error("[onMessageHandler] Error while inventory created . Err {}", e.getMessage());
        }
    }
}
