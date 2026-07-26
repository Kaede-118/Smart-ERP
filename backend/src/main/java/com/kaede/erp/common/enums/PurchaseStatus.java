package com.kaede.erp.common.enums;


public enum PurchaseStatus {


    DRAFT("草稿"),
    PENDING("待审核"),
    APPROVED("已审核"),
    RECEIVED("已入库"),
    CANCELLED("已取消");


    private final String displayName;


    PurchaseStatus(String displayName) {
        this.displayName = displayName;
    }


    public String getDisplayName() {
        return displayName;
    }

}
