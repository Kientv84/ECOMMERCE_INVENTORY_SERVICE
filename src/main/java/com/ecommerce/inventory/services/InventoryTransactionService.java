package com.ecommerce.inventory.services;

import com.ecommerce.inventory.commons.TransactionType;
import com.ecommerce.inventory.dtos.responses.InventoryTransactionResponse;

import java.util.List;
import java.util.UUID;

public interface InventoryTransactionService {

    List<InventoryTransactionResponse> getTransactionsByProductId(UUID productId);

    List<InventoryTransactionResponse> getTransactionsByOrderId(UUID orderId);

    List<InventoryTransactionResponse> getAllTransactions();
}
