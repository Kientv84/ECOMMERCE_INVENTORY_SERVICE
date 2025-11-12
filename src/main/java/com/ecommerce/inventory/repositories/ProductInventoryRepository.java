package com.ecommerce.inventory.repositories;

import com.ecommerce.inventory.entities.ProductInventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductInventoryRepository extends JpaRepository<ProductInventoryEntity, UUID> {
    ProductInventoryEntity findProductInventoryByProductId(UUID productId);
}
