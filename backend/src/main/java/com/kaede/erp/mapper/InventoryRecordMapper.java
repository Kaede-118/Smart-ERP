package com.kaede.erp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaede.erp.entity.InventoryRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


@Mapper
public interface InventoryRecordMapper extends BaseMapper<InventoryRecord> {


    @Select("SELECT r.*, p.name AS product_name, u.nickname AS operator_name " +
            "FROM inventory_record r " +
            "JOIN product p ON r.product_id = p.id " +
            "LEFT JOIN sys_user u ON r.operator_id = u.id " +
            "WHERE (#{productId} IS NULL OR r.product_id = #{productId}) " +
            "AND (#{type} IS NULL OR r.type = #{type}) " +
            "ORDER BY r.create_time DESC")
    List<Map<String, Object>> selectRecordList(
            @Param("productId") Long productId,
            @Param("type") String type
    );

}
