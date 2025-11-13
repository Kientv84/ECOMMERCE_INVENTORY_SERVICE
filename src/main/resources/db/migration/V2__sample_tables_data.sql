-- ======================================
-- V2__insert_inventory_sample_data.sql
-- ======================================

INSERT INTO product_inventory_entity (
    id,
    product_id,
    product_name,
    quantity_available,
    quantity_reserved,
    quantity_sold,
    created_by,
    updated_by
)
VALUES (
    gen_random_uuid(), -- ID của bản ghi inventory
    'c5045de3-a912-4d2a-88f7-d9203ffc378b',
    'Apex Seamless T-Shirt',
    50,
    0,
    0,
    'SYSTEM',
    'SYSTEM'
)
ON CONFLICT DO NOTHING;
