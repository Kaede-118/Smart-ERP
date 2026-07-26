package com.kaede.erp.common.enums;


public enum SalesStatus {


    PENDING("待确认"),
    CONFIRMED("已确认"),
    SHIPPED("已发货"),
    COMPLETED("已完成"),
    CANCELLED("已取消");


    private final String displayName;


    SalesStatus(String displayName) {
        this.displayName = displayName;
    }


    public String getDisplayName() {
        return displayName;
    }

}
