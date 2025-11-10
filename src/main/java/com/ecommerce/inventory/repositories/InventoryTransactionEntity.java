package com.ecommerce.inventory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InventoryTransactionEntity extends JpaRepository<com.ecommerce.inventory.entities.InventoryTransactionEntity, UUID> {
}
