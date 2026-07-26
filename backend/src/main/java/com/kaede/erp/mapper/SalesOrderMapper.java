package com.kaede.erp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaede.erp.entity.SalesOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;


@Mapper
public interface SalesOrderMapper extends BaseMapper<SalesOrder> {


    @Select("SELECT o.*, c.name AS customer_name, u.nickname AS creator_name " +
            "FROM sales_order o " +
            "JOIN customer c ON o.customer_id = c.id " +
            "LEFT JOIN sys_user u ON o.creator_id = u.id " +
            "WHERE o.deleted = 0 " +
            "ORDER BY o.create_time DESC")
    List<Map<String, Object>> selectOrderList();


    @Select("SELECT o.*, c.name AS customer_name, u.nickname AS creator_name " +
            "FROM sales_order o " +
            "JOIN customer c ON o.customer_id = c.id " +
            "LEFT JOIN sys_user u ON o.creator_id = u.id " +
            "WHERE o.id = #{id}")
    Map<String, Object> selectOrderDetail(@Param("id") Long id);


    @Update("UPDATE sales_order SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);

}
