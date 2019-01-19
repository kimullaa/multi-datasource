package com.example.demo;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class TransactionConfig {

    @Primary
    @Bean
    public PlatformTransactionManager platformTransactionManager(
            @Qualifier("primaryPlatformTransactionManager") PlatformTransactionManager tm1,
            @Qualifier("secondaryPlatformTransactionManager") PlatformTransactionManager tm2
    ) {
        return new ChainedTransactionManager(tm1, tm2);
    }
}
