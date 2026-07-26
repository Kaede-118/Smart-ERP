package com.kaede.erp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaede.erp.entity.Expense;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Mapper
public interface ExpenseMapper extends BaseMapper<Expense> {


    @Select("SELECT e.*, u.nickname AS creator_name FROM expense e " +
            "LEFT JOIN sys_user u ON e.create_by = u.id " +
            "ORDER BY e.create_time DESC")
    List<Map<String, Object>> selectExpenseList();


    @Select("SELECT e.*, u.nickname AS creator_name FROM expense e " +
            "LEFT JOIN sys_user u ON e.create_by = u.id " +
            "WHERE e.id = #{id}")
    Map<String, Object> selectExpenseDetail(@Param("id") Long id);


    @Select("SELECT COALESCE(SUM(amount), 0) FROM expense WHERE status IN ('APPROVED','PAID') " +
            "AND create_time >= DATE_FORMAT(CURDATE(), '%Y-%m-01')")
    BigDecimal monthExpense();


    @Select("SELECT COUNT(*) FROM expense WHERE status = 'PENDING'")
    long pendingCount();


    @Select("SELECT COUNT(*) FROM expense WHERE status = 'PAID'")
    long paidCount();


    @Select("SELECT COALESCE(SUM(amount), 0) FROM expense WHERE status IN ('APPROVED','PAID') " +
            "AND DATE(create_time) = CURDATE()")
    BigDecimal todayExpense();

}
