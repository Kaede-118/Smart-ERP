package com.kaede.erp.common.enums;


public enum BusinessType {


    PURCHASE("采购入库"),
    SALES("销售出库"),
    RETURN("退货"),
    ADJUST("盘点调整");


    private final String displayName;


    BusinessType(String displayName) {
        this.displayName = displayName;
    }


    public String getDisplayName() {
        return displayName;
    }

}
