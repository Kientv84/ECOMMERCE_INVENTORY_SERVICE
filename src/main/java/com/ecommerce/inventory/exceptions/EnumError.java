package com.ecommerce.inventory.exceptions;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum EnumError {

    INVENTORY_DATA_EXISTED("INVENTORY-DTE", "Data exit", HttpStatus.CONFLICT),
    INVENTORY_GET_ERROR("INVENTORY-GET-ERROR", "Have error in process get inventory", HttpStatus.BAD_REQUEST),
    INVENTORY_ERR_NOT_FOUND("INVENTORY-CATE_NF", "Not found sub inventory with id", HttpStatus.BAD_REQUEST),
    INVENTORY_ERR_DEL_EM("INVENTORY-CATE-GA", "List ids to delete is empty", HttpStatus.BAD_REQUEST),
    INVENTORY_NOT_FOUND("INVENTORY-404", "inventory not found", HttpStatus.NOT_FOUND),
    INVENTORY_SERVICE_UNAVAILABLE("INVENTORY-503", "inventory service unavailable", HttpStatus.SERVICE_UNAVAILABLE),

    INVALID_INVENTORY_METHOD("INVALID-METHOD", "Invalid inventory method", HttpStatus.NOT_FOUND),
    INVENTORY_NOT_ENOUGH("INVENTORY_NOT_ENOUGH", "Not enough in inventory", HttpStatus.BAD_REQUEST),

    INVENTORY_PROCESS_NOTNULL("INVENTORY-PROCESS-NULL", "inventory argument be null!", HttpStatus.BAD_REQUEST),
    //----------- EXTERNAL SERVICES ------------
    INTERNAL_ERROR("INVENTORY-S-999", "Unexpected internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    private final String code;
    private final String defaultMessage;
    private final HttpStatus httpStatus;


    public static EnumError fromCode(String code) {
        for (EnumError e : values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Unknown DispatchError code: " + code);
    }
}

