package com.ecommerce.inventory.messaging.consumers;

import com.ecommerce.inventory.services.ProductInventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class PaymentChecked {
    private final ProductInventoryService productInventoryService;

    @KafkaListener(
            topics = "${spring.kafka.payment.topic.payment-checked}",
            groupId = "spring.kafka.payment.group",
            containerFactory = "kafkaListenerContainerFactory")
    public void onMessageHandler(@Payload Kafka message) {
        try {
            log.info("[onMessageHandler] Start consuming message ...");
            log.info("[onMessageHandler] Received message payload: {}", message);
            paymentService.processPayment(message);
            log.info("[onMessageHandler] Process payment success ...");
        } catch (Exception e) {
            log.error("[onMessageHandler] Error while Process payment . Err {}", e.getMessage());
        }
    }
}
