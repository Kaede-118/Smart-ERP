package com.kaede.erp.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.kaede.erp.mapper")
public class MybatisPlusConfig {

}