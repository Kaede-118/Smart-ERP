package com.kaede.erp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaede.erp.entity.Inventory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


@Mapper
public interface InventoryMapper extends BaseMapper<Inventory> {


    @Select("SELECT i.*, p.name AS product_name, p.code AS product_code, " +
            "pc.name AS category_name " +
            "FROM inventory i " +
            "JOIN product p ON i.product_id = p.id " +
            "LEFT JOIN product_category pc ON p.category_id = pc.id " +
            "WHERE p.deleted = 0 " +
            "AND (#{keyword} IS NULL OR #{keyword} = '' OR p.name LIKE CONCAT('%', #{keyword}, '%') OR p.code LIKE CONCAT('%', #{keyword}, '%')) " +
            "AND (#{categoryId} IS NULL OR p.category_id = #{categoryId}) " +
            "ORDER BY i.update_time DESC")
    List<Map<String, Object>> selectInventoryList(
            @Param("keyword") String keyword,
            @Param("categoryId") Long categoryId
    );


    @Select("SELECT i.*, p.name AS product_name, p.code AS product_code, " +
            "pc.name AS category_name " +
            "FROM inventory i " +
            "JOIN product p ON i.product_id = p.id " +
            "LEFT JOIN product_category pc ON p.category_id = pc.id " +
            "WHERE i.product_id = #{productId} AND p.deleted = 0")
    Map<String, Object> selectInventoryDetail(@Param("productId") Long productId);

}
