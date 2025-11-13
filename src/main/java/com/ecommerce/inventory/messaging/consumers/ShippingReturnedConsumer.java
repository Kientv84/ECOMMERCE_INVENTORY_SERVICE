package com.ecommerce.inventory.messaging.consumers;

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
public class ShippingReturnedConsumer {
    private final ProductInventoryService productInventoryService;

    @KafkaListener(
            topics = "${spring.kafka.shipping.topic.shipping-returned}",
            groupId = "spring.kafka.shipping.group",
            containerFactory = "kafkaListenerContainerFactory")
    public void onMessageHandler(@Payload String message) {
        try {
            log.info("[ShippingReturnedConsumer] Start consuming message ...");
            log.info("[ShippingReturnedConsumer] Received message payload: {}", message);

            KafkaEventInventory response = new ObjectMapper().readValue(message, KafkaEventInventory.class);

            productInventoryService.releaseReserved(response);
            log.info("[ShippingReturnedConsumer] Process inventory deduct ...");
        } catch (Exception e) {
            log.error("[onMessageHandler] Error while inventory deduct . Err {}", e.getMessage());
        }
    }
}
