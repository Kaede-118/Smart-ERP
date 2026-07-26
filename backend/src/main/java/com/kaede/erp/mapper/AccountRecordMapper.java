package com.kaede.erp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaede.erp.entity.AccountRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Mapper
public interface AccountRecordMapper extends BaseMapper<AccountRecord> {


    @Select("SELECT COALESCE(SUM(change_amount), 0) FROM account_record " +
            "WHERE type = 'INCOME' AND DATE(create_time) = CURDATE()")
    BigDecimal todayIncome();


    @Select("SELECT COALESCE(SUM(change_amount), 0) FROM account_record " +
            "WHERE type = 'EXPENSE' AND DATE(create_time) = CURDATE()")
    BigDecimal todayExpense();


    @Select("SELECT DATE(create_time) AS date, " +
            "SUM(CASE WHEN type = 'INCOME' THEN change_amount ELSE 0 END) AS income, " +
            "SUM(CASE WHEN type = 'EXPENSE' THEN change_amount ELSE 0 END) AS expense " +
            "FROM account_record WHERE create_time >= #{since} " +
            "GROUP BY DATE(create_time) ORDER BY DATE(create_time)")
    List<Map<String, Object>> selectTrend(@Param("since") String since);

}
