-- ======================================
-- V1__create_inventory_tables.sql
-- ======================================

-- 1. Bảng product_inventory_entity
CREATE TABLE IF NOT EXISTS product_inventory_entity (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id UUID NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    quantity_available INT NOT NULL DEFAULT 0,
    quantity_reserved INT NOT NULL DEFAULT 0,
    quantity_sold INT NOT NULL DEFAULT 0,

    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);

-- 2. Bảng inventory_transaction_entity
CREATE TABLE IF NOT EXISTS inventory_transaction_entity (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity INT NOT NULL,
    type VARCHAR(20) NOT NULL, -- DEDUCT, RESTORE, ADJUST
    source VARCHAR(100),

    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);

-- Optional: index để truy vấn nhanh
CREATE INDEX IF NOT EXISTS idx_inventory_product ON product_inventory_entity(product_id);
CREATE INDEX IF NOT EXISTS idx_inventory_transaction_product ON inventory_transaction_entity(product_id);
CREATE INDEX IF NOT EXISTS idx_inventory_transaction_order ON inventory_transaction_entity(order_id);