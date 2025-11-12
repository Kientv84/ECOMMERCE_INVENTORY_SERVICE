package com.ecommerce.inventory.services.impls;

import com.ecommerce.inventory.commons.TransactionType;
import com.ecommerce.inventory.dtos.requests.ProductInventoryRequest;
import com.ecommerce.inventory.dtos.requests.ProductInventoryUpdateRequest;
import com.ecommerce.inventory.dtos.requests.kafka.KafkaInventoryRequest;
import com.ecommerce.inventory.dtos.responses.ProductInventoryResponse;
import com.ecommerce.inventory.dtos.responses.kafka.KafkaEventInventory;
import com.ecommerce.inventory.dtos.responses.kafka.KafkaShipmentItemResponse;
import com.ecommerce.inventory.entities.ProductInventoryEntity;
import com.ecommerce.inventory.exceptions.EnumError;
import com.ecommerce.inventory.exceptions.ServiceException;
import com.ecommerce.inventory.mappers.ProductInventoryMapper;
import com.ecommerce.inventory.repositories.ProductInventoryRepository;
import com.ecommerce.inventory.services.ProductInventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductInventoryServiceImpl implements ProductInventoryService {
    private final ProductInventoryRepository productInventoryRepository;
    private final ProductInventoryMapper productInventoryMapper;

    @Override
    public ProductInventoryResponse create(KafkaInventoryRequest message) {
        log.info("Listen create product inventory by product service");
        try {
            ProductInventoryEntity productInventory = productInventoryRepository.findProductInventoryByProductId(message.getProductId());

            if ( productInventory != null ) {
                throw new ServiceException(EnumError.INVENTORY_DATA_EXISTED, "inventory.data.exit");
            }

            ProductInventoryEntity productInventoryEntity = ProductInventoryEntity.builder()
                    .productName(message.getProductName())
                    .productId(message.getProductId())
                    .quantityAvailable(100)
                    .quantityReserved(0)
                    .quantitySold(0)
                    .build();

            productInventoryRepository.save(productInventoryEntity);

            return productInventoryMapper.mapToProductInventoryResponse(productInventoryEntity);

        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    public List<ProductInventoryResponse> getAll() {
        try {
            List<ProductInventoryResponse> responses = productInventoryRepository.findAll().stream().map(inventory -> productInventoryMapper.mapToProductInventoryResponse(inventory)).toList();

            return responses;
        } catch (Exception e) {
            throw new ServiceException(EnumError.INVENTORY_GET_ERROR, "inventory.get.error");
        }
    }

    @Override
    public ProductInventoryResponse getById(UUID uuid) {
        try {
            ProductInventoryEntity productInventory = productInventoryRepository.findById(uuid).orElseThrow(()-> new ServiceException(EnumError.INVENTORY_GET_ERROR, "inventory.get.error"));

            return productInventoryMapper.mapToProductInventoryResponse(productInventory);

        } catch ( ServiceException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProductInventoryResponse updateById(UUID uuid, ProductInventoryUpdateRequest updateRequest) {
        return null  ;
    }

    @Override
    public String delete(List<UUID> ids) {
        try {
            if ( ids == null || ids.isEmpty()) {
                throw new ServiceException(EnumError.INVENTORY_ERR_DEL_EM, "inventory.delete.empty");
            }

            List<ProductInventoryEntity> foundIds = productInventoryRepository.findAllById(ids);

            System.out.println("Find collection:" + foundIds.toString());

            if ( foundIds.isEmpty()) {
                throw new ServiceException(EnumError.INVENTORY_ERR_NOT_FOUND, "inventory.delete.nottfound");
            }

            productInventoryRepository.deleteAllById(ids);

            return "Deleted product inventory successfully: {}" + ids;

        } catch (Exception e) {
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    public void recordTransaction(UUID orderId, UUID productId, int quantity, TransactionType type, String source) {

    }

    @Override
    public void deductInventory(KafkaEventInventory message) {
        try {
            List<KafkaShipmentItemResponse> listItems = message.getItems();

//            ProductInventoryEntity productInventory = productInventoryRepository.findProductInventoryByProductId(message.getItems());

        } catch (Exception e) {
            throw new ServiceException(EnumError.INVENTORY_GET_ERROR,  "sys.internal.error");
        }
    }

    @Override
    public void reserveInventory(KafkaEventInventory message) {

    }

    @Override
    public void releaseReserved(KafkaEventInventory message) {

    }

    @Override
    public void confirmSold(KafkaEventInventory message) {

    }
}
