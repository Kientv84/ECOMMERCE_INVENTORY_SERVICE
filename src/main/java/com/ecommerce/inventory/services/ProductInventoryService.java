package com.ecommerce.inventory.services;

import com.ecommerce.inventory.commons.TransactionType;
import com.ecommerce.inventory.dtos.requests.ProductInventoryRequest;
import com.ecommerce.inventory.dtos.requests.ProductInventoryUpdateRequest;
import com.ecommerce.inventory.dtos.requests.kafka.KafkaInventoryRequest;
import com.ecommerce.inventory.dtos.responses.ProductInventoryResponse;
import com.ecommerce.inventory.dtos.responses.kafka.KafkaEventInventory;

import java.util.List;
import java.util.UUID;

public interface ProductInventoryService {
    ProductInventoryResponse create(KafkaInventoryRequest message);

    List<ProductInventoryResponse> getAll();

    ProductInventoryResponse getById(UUID uuid);

    ProductInventoryResponse updateById(UUID uuid, ProductInventoryUpdateRequest updateRequest);

    String delete(List<UUID> ids);

    void recordTransaction(UUID orderId, UUID productId, int quantity, TransactionType type, String source);

    void deductInventory(KafkaEventInventory message);      // Trừ kho khi thanh toán

    void reserveInventory(KafkaEventInventory message);     // Giữ hàng (COD hoặc đơn online)

    void releaseReserved(KafkaEventInventory message);      // Hoàn kho khi hủy hoặc bom hàng

    void confirmSold(KafkaEventInventory message);

}

