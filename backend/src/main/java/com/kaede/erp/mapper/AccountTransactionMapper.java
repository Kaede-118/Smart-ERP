package com.kaede.erp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaede.erp.entity.AccountTransaction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Mapper
public interface AccountTransactionMapper extends BaseMapper<AccountTransaction> {


    @Select("SELECT COALESCE(SUM(CAST(change_amount AS DECIMAL(14,2))), 0) FROM account_transaction " +
            "WHERE change_type = 'IN' AND DATE(create_time) = CURDATE()")
    BigDecimal todayIncome();


    @Select("SELECT COALESCE(SUM(CAST(change_amount AS DECIMAL(14,2))), 0) FROM account_transaction " +
            "WHERE change_type = 'OUT' AND DATE(create_time) = CURDATE()")
    BigDecimal todayExpense();


    @Select("SELECT DATE(create_time) AS date, " +
            "SUM(CASE WHEN change_type = 'IN' THEN CAST(change_amount AS DECIMAL(14,2)) ELSE 0 END) AS income, " +
            "SUM(CASE WHEN change_type = 'OUT' THEN CAST(change_amount AS DECIMAL(14,2)) ELSE 0 END) AS expense " +
            "FROM account_transaction WHERE create_time >= #{since} " +
            "GROUP BY DATE(create_time) ORDER BY DATE(create_time)")
    List<Map<String, Object>> selectTrend(@Param("since") String since);


    @Select("SELECT t.* FROM account_transaction t " +
            "WHERE (#{type} IS NULL OR t.change_type = #{type}) " +
            "AND (#{businessType} IS NULL OR t.business_type = #{businessType}) " +
            "ORDER BY t.create_time DESC " +
            "LIMIT #{limit} OFFSET #{offset}")
    List<AccountTransaction> selectTransactionList(
            @Param("type") String type,
            @Param("businessType") String businessType,
            @Param("limit") int limit,
            @Param("offset") int offset
    );


    @Select("SELECT COUNT(*) FROM account_transaction " +
            "WHERE (#{type} IS NULL OR change_type = #{type}) " +
            "AND (#{businessType} IS NULL OR business_type = #{businessType})")
    long countTransactions(
            @Param("type") String type,
            @Param("businessType") String businessType
    );

}
