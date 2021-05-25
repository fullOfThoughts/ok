package com.bmd.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class webConfig {
    @Bean
    public GlobalFilter customFilter() {
        return new CustomGlobalFilter();
    }
}
