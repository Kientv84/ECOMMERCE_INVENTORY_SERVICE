package com.ecommerce.inventory.services.impls;

import com.ecommerce.inventory.commons.TransactionType;
import com.ecommerce.inventory.dtos.requests.ProductInventoryRequest;
import com.ecommerce.inventory.dtos.requests.ProductInventoryUpdateRequest;
import com.ecommerce.inventory.dtos.requests.kafka.KafkaInventoryRequest;
import com.ecommerce.inventory.dtos.responses.ProductInventoryResponse;
import com.ecommerce.inventory.dtos.responses.kafka.KafkaEventInventory;
import com.ecommerce.inventory.dtos.responses.kafka.KafkaShipmentItemResponse;
import com.ecommerce.inventory.entities.InventoryTransactionEntity;
import com.ecommerce.inventory.entities.ProductInventoryEntity;
import com.ecommerce.inventory.exceptions.EnumError;
import com.ecommerce.inventory.exceptions.ServiceException;
import com.ecommerce.inventory.mappers.ProductInventoryMapper;
import com.ecommerce.inventory.repositories.InventoryTransactionRepository;
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
    private final InventoryTransactionRepository inventoryTransactionRepository;

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
        try {
            InventoryTransactionEntity inventoryTransactionEntity = InventoryTransactionEntity.builder()
                    .source(source)
                    .quantity(quantity)
                    .productId(productId)
                    .orderId(orderId)
                    .type(type)
                    .build();

            inventoryTransactionRepository.save(inventoryTransactionEntity);

        } catch (Exception e) {
            throw new ServiceException(EnumError.INVENTORY_GET_ERROR,  "sys.internal.error");
        }
    }

    @Override
    public void deductInventory(KafkaEventInventory message) {
        log.info("Calling form shipping shipped service");
        try {

            for ( KafkaShipmentItemResponse itemResponse :  message.getItems()) {
                UUID productId = itemResponse.getProductId();
                String productName = itemResponse.getProductName();
                Integer quantity = itemResponse.getQuantity();

                ProductInventoryEntity productFound = productInventoryRepository.findProductInventoryByProductId(productId);

                if (productFound == null) {
                    log.error("[InventoryService] Product not found in inventory: {}", productId);
                    throw new ServiceException(EnumError.INVENTORY_NOT_FOUND, "inventory.product.not.found");
                }

                // Kiểm tra đủ hàng hay không
                if (productFound.getQuantityAvailable() < quantity) {
                    log.warn("[InventoryService] Not enough stock for product {}. Available={}, Requested={}",
                            productFound.getProductName(),
                            productFound.getQuantityAvailable(),
                            quantity);
                    throw new ServiceException(EnumError.INVENTORY_NOT_ENOUGH, "inventory.not.enough.stock");
                }

                // Trừ đi số lượng tồn tại
                int newAvailable = productFound.getQuantityAvailable() - quantity;
                // Tăng số lượng giữ hàng
                int newReserved = productFound.getQuantityReserved() + quantity;


                productFound.setQuantityAvailable(newAvailable);
                productFound.setQuantityReserved(newReserved);

                productInventoryRepository.save(productFound);

                recordTransaction(message.getOrderId(), productId, quantity, TransactionType.DEDUCT, "shipping service - shipped");

                log.info("[InventoryService] Updated inventory for product {}: remaining={}",
                        productFound.getProductName(),
                        productFound.getQuantityAvailable());
            }

            log.info("[InventoryService] Inventory deduction completed for order: {}", message.getOrderId());

        } catch ( ServiceException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ServiceException(EnumError.INVENTORY_GET_ERROR,  "sys.internal.error");
        }
    }


    @Override
    public void releaseReserved(KafkaEventInventory message) {
        log.info("Calling form shipping returned service");

        try {
            for ( KafkaShipmentItemResponse itemResponse : message.getItems()) {
                UUID productId = itemResponse.getProductId();
                String productName = itemResponse.getProductName();
                Integer quantity = itemResponse.getQuantity();

                ProductInventoryEntity productFound = productInventoryRepository.findProductInventoryByProductId(productId);

                if (productFound == null) {
                    log.error("[InventoryService] Product not found in inventory at restore: {}", productId);
                    throw new ServiceException(EnumError.INVENTORY_NOT_FOUND, "inventory.product.not.found");
                }

                // Hồi số lượng tồn tại
                int newAvailable = productFound.getQuantityAvailable() + quantity;
                // giảm số lượng giữ hàng
                int newReserved = productFound.getQuantityReserved() - quantity;


                productFound.setQuantityAvailable(newAvailable);
                productFound.setQuantityReserved(newReserved);

                productInventoryRepository.save(productFound);

                recordTransaction(message.getOrderId(), productId, quantity, TransactionType.RESTORE, "shipping service - returned");

                log.info("[InventoryService] Updated inventory restore for product {}: remaining={}",
                        productFound.getProductName(),
                        productFound.getQuantityAvailable());
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(EnumError.INVENTORY_GET_ERROR, "sys.internal.error");
        }
    }

    @Override
    public void confirmSold(KafkaEventInventory message) {
        log.info("Calling form shipping completed service");
        try {
            for ( KafkaShipmentItemResponse itemResponse : message.getItems()) {
                UUID productId = itemResponse.getProductId();
                String productName = itemResponse.getProductName();
                Integer quantity = itemResponse.getQuantity();

                ProductInventoryEntity productFound = productInventoryRepository.findProductInventoryByProductId(productId);
                InventoryTransactionEntity transactionFound = inventoryTransactionRepository.findInventoryTransactionByOrderId(message.getOrderId());

                if (productFound == null) {
                    log.error("[InventoryService] Product not found in inventory at confirm sold: {}", productId);
                    throw new ServiceException(EnumError.INVENTORY_NOT_FOUND, "inventory.product.not.found");
                }

                if (transactionFound.getType() == TransactionType.DEDUCT) {
                    log.error("[InventoryService] Product found in inventory at deduct: {}", productId);
                    // Hồi số lượng giữ hàng
                    int newReserved = productFound.getQuantityReserved() - quantity;
                    // Tăng số lượng đã bán
                    int newSold= productFound.getQuantitySold() + quantity;

                    productFound.setQuantityReserved(newReserved);
                    productFound.setQuantitySold(newSold);
                }

                productInventoryRepository.save(productFound);

                recordTransaction(message.getOrderId(), productId, quantity, TransactionType.DEDUCT, "shipping service - completed");

                log.info("[InventoryService] Updated inventory  confirm sold for product {}: remaining={}",
                        productFound.getProductName(),
                        productFound.getQuantityAvailable());
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(EnumError.INVENTORY_GET_ERROR, "sys.internal.error");
        }
    }
}
