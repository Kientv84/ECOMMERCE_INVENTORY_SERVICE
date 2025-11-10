package com.ecommerce.inventory.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
public class ProductInventoryRequest {
    @NotNull(message = "{product.inventory.productId.notnull}")
    private UUID productId;
    @NotNull(message = "{product.inventory.productName.notnull}")
    private String productName;
    @NotNull(message = "{product.inventory.quantityAvailable.notnull}")
    private int quantityAvailable;
}
