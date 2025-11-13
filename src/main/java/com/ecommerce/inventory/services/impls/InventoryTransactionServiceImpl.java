package com.ecommerce.inventory.services.impls;

import com.ecommerce.inventory.dtos.responses.InventoryTransactionResponse;
import com.ecommerce.inventory.services.InventoryTransactionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class InventoryTransactionServiceImpl implements InventoryTransactionService {
    @Override
    public List<InventoryTransactionResponse> getTransactionsByProductId(UUID productId) {
        return List.of();
    }

    @Override
    public List<InventoryTransactionResponse> getTransactionsByOrderId(UUID orderId) {
        return List.of();
    }

    @Override
    public List<InventoryTransactionResponse> getAllTransactions() {
        return List.of();
    }
}
