package com.kaede.erp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaede.erp.entity.SalesItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


@Mapper
public interface SalesItemMapper extends BaseMapper<SalesItem> {


    @Select("SELECT i.*, p.name AS product_name, p.code AS product_code " +
            "FROM sales_item i " +
            "JOIN product p ON i.product_id = p.id " +
            "WHERE i.order_id = #{orderId}")
    List<Map<String, Object>> selectItemsByOrderId(@Param("orderId") Long orderId);

}
