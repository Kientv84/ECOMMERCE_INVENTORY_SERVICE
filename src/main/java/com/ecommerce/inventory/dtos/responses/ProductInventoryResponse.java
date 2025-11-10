package com.ecommerce.inventory.dtos.responses;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;
import java.util.UUID;


@Setter
@Getter
@AllArgsConstructor
public class ProductInventoryResponse {
    private UUID id;
    private UUID productId;
    private String productName;
    private int quantityAvailable;
    private int quantityReserved;
    private int quantitySold;
    private Date createdDate;
}
