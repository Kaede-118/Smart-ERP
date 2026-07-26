package com.kaede.erp.mapper;

import com.kaede.erp.dto.DashboardTrendDTO;
import com.kaede.erp.vo.DashboardWarningVO;
import com.kaede.erp.vo.TopProductVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;


@Mapper
public interface DashboardMapper {


    @Select("SELECT COUNT(*) FROM product WHERE deleted = 0")
    long countProduct();


    @Select("SELECT COUNT(*) FROM customer WHERE deleted = 0")
    long countCustomer();


    @Select("SELECT COUNT(*) FROM supplier WHERE deleted = 0")
    long countSupplier();


    @Select("SELECT COALESCE(SUM(quantity), 0) FROM inventory")
    long sumInventoryQuantity();


    @Select("SELECT COALESCE(SUM(total_amount), 0) FROM purchase_order " +
            "WHERE status = 'RECEIVED' AND DATE(create_time) = CURDATE()")
    BigDecimal todayPurchaseAmount();


    @Select("SELECT COALESCE(SUM(total_amount), 0) FROM sales_order " +
            "WHERE status = 'COMPLETED' AND DATE(create_time) = CURDATE()")
    BigDecimal todaySaleAmount();


    @Select("SELECT COUNT(*) FROM inventory WHERE quantity < warning_value")
    long countLowStock();


    @Select("SELECT i.product_id AS productId, p.name AS productName, p.code AS productCode, " +
            "i.quantity, i.warning_value AS warningValue " +
            "FROM inventory i JOIN product p ON i.product_id = p.id " +
            "WHERE i.quantity < i.warning_value AND p.deleted = 0")
    List<DashboardWarningVO> selectLowStockList();


    @Select("SELECT DATE(create_time) AS date, " +
            "COALESCE(SUM(total_amount), 0) AS purchaseAmount, 0 AS saleAmount " +
            "FROM purchase_order WHERE status = 'RECEIVED' " +
            "AND create_time >= #{since} GROUP BY DATE(create_time) " +
            "ORDER BY DATE(create_time)")
    List<DashboardTrendDTO> selectPurchaseTrend(@Param("since") String since);


    @Select("SELECT DATE(create_time) AS date, " +
            "0 AS purchaseAmount, COALESCE(SUM(total_amount), 0) AS saleAmount " +
            "FROM sales_order WHERE status = 'COMPLETED' " +
            "AND create_time >= #{since} GROUP BY DATE(create_time) " +
            "ORDER BY DATE(create_time)")
    List<DashboardTrendDTO> selectSalesTrend(@Param("since") String since);


    @Select("SELECT si.product_id AS productId, p.name AS productName, p.code AS productCode, " +
            "SUM(si.quantity) AS saleQuantity " +
            "FROM sales_item si " +
            "JOIN sales_order so ON si.order_id = so.id " +
            "JOIN product p ON si.product_id = p.id " +
            "WHERE so.status = 'COMPLETED' " +
            "GROUP BY si.product_id ORDER BY saleQuantity DESC LIMIT 10")
    List<TopProductVO> selectTopProducts();

}
