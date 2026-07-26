package com.kaede.erp.common.enums;


public enum InventoryType {


    INBOUND("入库"),
    OUTBOUND("出库"),
    ADJUST("调整");


    private final String displayName;


    InventoryType(String displayName) {
        this.displayName = displayName;
    }


    public String getDisplayName() {
        return displayName;
    }

}
