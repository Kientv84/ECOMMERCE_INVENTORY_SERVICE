package com.ecommerce.inventory.controllers;

import com.ecommerce.inventory.dtos.requests.ProductInventoryRequest;
import com.ecommerce.inventory.dtos.responses.ProductInventoryResponse;
import com.ecommerce.inventory.services.ProductInventoryService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/v1/api")
@RequiredArgsConstructor
public class ProductInventoryController {
    private final ProductInventoryService productInventoryService;

    @GetMapping("/product-inventories")
    public ResponseEntity<List<ProductInventoryResponse>> getAllProductInventory() {
        return ResponseEntity.ok(productInventoryService.getAll());
    }

    @GetMapping("/product-inventory/{id}")
    public ResponseEntity<ProductInventoryResponse> getProductInventoryById(@PathVariable UUID id) {
        return ResponseEntity.ok(productInventoryService.getById(id));
    }

    @PostMapping("/product-inventories")
    public String deleteProductInventory(@RequestBody List<UUID> uuids) {
        return productInventoryService.delete(uuids);
    }
}
