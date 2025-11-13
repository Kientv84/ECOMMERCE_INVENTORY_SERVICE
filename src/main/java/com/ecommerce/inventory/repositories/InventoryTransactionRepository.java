package com.ecommerce.inventory.repositories;

import com.ecommerce.inventory.entities.InventoryTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InventoryTransactionRepository extends JpaRepository<com.ecommerce.inventory.entities.InventoryTransactionEntity, UUID> {
    InventoryTransactionEntity findInventoryTransactionByOrderId(UUID orderId);
}
