package com.ecommerce.inventory.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "product_inventory_entity")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ProductInventoryEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(name = "product_id", nullable = false)
    private UUID productId; // liên kết tới product service

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "quantity_available", nullable = false)
    private int quantityAvailable; // tồn kho hiện tại

    @Column(name = "quantity_reserved", nullable = false)
    private int quantityReserved; // số lượng đã giữ (nếu dùng pre-reserve)

    @Column(name = "quantity_sold", nullable = false)
    private int quantitySold; // tổng đã bán


    // ====== Metadata ======
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name ="create_date")
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    @Column(name ="update_date")
    private Date updatedDate;

    @CreatedBy
    @Column(name ="created_by")
    private String createdBy;

    @LastModifiedBy
    @Column(name ="updated_by")
    private String updatedBy;
}

