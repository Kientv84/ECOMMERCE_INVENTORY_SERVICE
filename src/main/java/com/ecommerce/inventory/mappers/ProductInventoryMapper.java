package com.ecommerce.inventory.mappers;

import com.ecommerce.inventory.dtos.responses.ProductInventoryResponse;
import com.ecommerce.inventory.entities.ProductInventoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductInventoryMapper {
    ProductInventoryResponse mapToProductInventoryResponse(ProductInventoryEntity productInventoryEntity);
}
