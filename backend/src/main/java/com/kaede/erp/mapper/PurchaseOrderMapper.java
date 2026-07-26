package com.kaede.erp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaede.erp.entity.PurchaseOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;


@Mapper
public interface PurchaseOrderMapper extends BaseMapper<PurchaseOrder> {


    @Select("SELECT o.*, s.name AS supplier_name, u.nickname AS creator_name " +
            "FROM purchase_order o " +
            "JOIN supplier s ON o.supplier_id = s.id " +
            "LEFT JOIN sys_user u ON o.creator_id = u.id " +
            "WHERE o.deleted = 0 " +
            "ORDER BY o.create_time DESC")
    List<Map<String, Object>> selectOrderList();


    @Select("SELECT o.*, s.name AS supplier_name, u.nickname AS creator_name " +
            "FROM purchase_order o " +
            "JOIN supplier s ON o.supplier_id = s.id " +
            "LEFT JOIN sys_user u ON o.creator_id = u.id " +
            "WHERE o.id = #{id}")
    Map<String, Object> selectOrderDetail(@Param("id") Long id);


    @Update("UPDATE purchase_order SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);

}
