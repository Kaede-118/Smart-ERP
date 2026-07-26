package com.kaede.erp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaede.erp.entity.Customer;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {

}
