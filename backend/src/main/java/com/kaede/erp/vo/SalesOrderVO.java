package com.kaede.erp.vo;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Data
public class SalesOrderVO {


    private Long id;


    private String orderNo;


    private Long customerId;


    private String customerName;


    private BigDecimal totalAmount;


    private String status;


    private String creatorName;


    private LocalDateTime createTime;


    private List<SalesItemVO> items;


    @SuppressWarnings("unchecked")
    public static SalesOrderVO fromMap(Map<String, Object> map) {

        if (map == null) {
            return null;
        }

        SalesOrderVO vo = new SalesOrderVO();

        vo.setId(toLong(map.get("id")));
        vo.setOrderNo((String) map.get("order_no"));
        vo.setCustomerId(toLong(map.get("customer_id")));
        vo.setCustomerName((String) map.get("customer_name"));
        vo.setTotalAmount((BigDecimal) map.get("total_amount"));
        vo.setStatus((String) map.get("status"));
        vo.setCreatorName((String) map.get("creator_name"));
        vo.setCreateTime((LocalDateTime) map.get("create_time"));

        return vo;
    }


    private static Long toLong(Object value) {
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return null;
    }

}
