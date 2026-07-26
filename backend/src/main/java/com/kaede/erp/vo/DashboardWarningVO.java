package com.kaede.erp.vo;


import lombok.Data;


@Data
public class DashboardWarningVO {


    private Long productId;

    private String productName;

    private String productCode;

    private Integer quantity;

    private Integer warningValue;

}
