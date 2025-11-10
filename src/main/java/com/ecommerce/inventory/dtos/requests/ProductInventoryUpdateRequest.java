package com.ecommerce.inventory.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
public class ProductInventoryUpdateRequest {
    private UUID productId;
    private String productName;
    private int quantityAvailable;
}
