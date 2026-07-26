package com.kaede.erp.vo;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;


@Data
public class InventoryRecordVO {


    private Long id;


    private Long productId;


    private String productName;


    private Integer changeQty;


    private Integer beforeQty;


    private Integer afterQty;


    private String type;


    private String businessType;


    private Long businessId;


    private String remark;


    private String operatorName;


    private LocalDateTime createTime;


    @SuppressWarnings("unchecked")
    public static InventoryRecordVO fromMap(Map<String, Object> map) {

        if (map == null) {
            return null;
        }

        InventoryRecordVO vo = new InventoryRecordVO();

        vo.setId(toLong(map.get("id")));
        vo.setProductId(toLong(map.get("product_id")));
        vo.setProductName((String) map.get("product_name"));
        vo.setChangeQty((Integer) map.get("change_qty"));
        vo.setBeforeQty((Integer) map.get("before_qty"));
        vo.setAfterQty((Integer) map.get("after_qty"));
        vo.setType((String) map.get("type"));
        vo.setBusinessType((String) map.get("business_type"));
        vo.setBusinessId(toLong(map.get("business_id")));
        vo.setRemark((String) map.get("remark"));
        vo.setOperatorName((String) map.get("operator_name"));
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
