package com.ecommerce.inventory.services;

import com.ecommerce.inventory.commons.TransactionType;
import com.ecommerce.inventory.dtos.requests.ProductInventoryRequest;
import com.ecommerce.inventory.dtos.requests.ProductInventoryUpdateRequest;
import com.ecommerce.inventory.dtos.responses.ProductInventoryResponse;

import java.util.List;
import java.util.UUID;

public interface ProductInventoryService {
    ProductInventoryResponse create(ProductInventoryRequest request);

    List<ProductInventoryResponse> getAll();

    ProductInventoryResponse getById(UUID uuid);

    ProductInventoryResponse updateById(UUID uuid, ProductInventoryUpdateRequest updateRequest);

    String delete(List<UUID> ids);

    void recordTransaction(UUID orderId, UUID productId, int quantity, TransactionType type, String source);

    void deductInventory(UUID orderId, List<ItemDTO> items, String source);      // Trừ kho khi thanh toán

    void reserveInventory(UUID orderId, List<ItemDTO> items, String source);     // Giữ hàng (COD hoặc đơn online)

    void releaseReserved(UUID orderId, List<ItemDTO> items, String reason);      // Hoàn kho khi hủy hoặc bom hàng

    void confirmSold(UUID orderId, List<ItemDTO> items);

}

