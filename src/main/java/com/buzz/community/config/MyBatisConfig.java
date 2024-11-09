package com.buzz.community.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.buzz.community.dao")
public class MyBatisConfig {
}
