package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class MybatisSecondaryConfig {
    private final DataSourceProperties dataSourceProperties;
    private final MybatisProperties mybatisProperties;

    @Bean(name = "secondaryDatasource")
    public DataSource ds() {
        return DataSourceBuilder.create()
                .driverClassName(dataSourceProperties.getDriverClassName())
                .password(dataSourceProperties.getPassword())
                .url(dataSourceProperties.getUrl().replaceFirst("dev", "ds1"))
                .username(dataSourceProperties.getUsername())
                .build();
    }

    @Bean(name = "secondarySqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(ds());
        factory.setVfs(SpringBootVFS.class);
        factory.setMapperLocations(mybatisProperties.resolveMapperLocations());
        return factory.getObject();
    }

    @Bean(name = "secondarySqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory());
    }

    @Bean(name = "secondaryPlatformTransactionManager")
    public PlatformTransactionManager platformTransactionManager() {
        return new DataSourceTransactionManager(ds());
    }

    @Bean(name = "oldSampleRepository")
    public SampleRepository sampleRepository() throws Exception {
        return sqlSessionTemplate().getMapper(SampleRepository.class);
    }


}
