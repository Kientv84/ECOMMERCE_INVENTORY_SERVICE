package com.ecommerce.inventory.dtos.responses;

import com.ecommerce.inventory.commons.TransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.UUID;

public class InventoryTransactionResponse {
    private UUID id;
    private UUID orderId;
    private UUID productId;
    private int quantity;
    private TransactionType type;  // DEDUCT, RESTORE, ADJUST
    private String source; // payment-service, order-service, manual, etc.
}
