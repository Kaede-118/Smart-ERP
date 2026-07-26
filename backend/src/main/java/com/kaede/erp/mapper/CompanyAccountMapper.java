package com.kaede.erp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaede.erp.entity.CompanyAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;


@Mapper
public interface CompanyAccountMapper extends BaseMapper<CompanyAccount> {


    @Update("UPDATE company_account SET balance = balance - #{amount} WHERE id = 1 AND balance >= #{amount}")
    int deductBalance(BigDecimal amount);


    @Update("UPDATE company_account SET balance = balance + #{amount} WHERE id = 1")
    int updateBalance(BigDecimal amount);

}
