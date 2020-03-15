package com.study.wxapp.config.dataSourceConfig;


import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @date 2018/5/3
 */
@Configuration
@EnableTransactionManagement(order = 2)
@MapperScan(basePackages = {"com.study.wxapp.mapper"})
public class MybatisConfig  implements TransactionManagementConfigurer, ApplicationContextAware {


    private static ApplicationContext context;


    /**
     * 写库数据源
     */
    @Autowired
    private DataSource dataSource;

    /**
     * 读数据源数量
     */
    @Value("${spring.db.readsize}")
    private Integer readsize;
    /**
     * 数据源路由代理
     *
     * @return
     */

    @Bean
    public AbstractRoutingDataSource routingDataSouceProxy() {
        RoutingDataSource proxy = new RoutingDataSource(readsize);
        Map<Object, Object> targetDataSources = new HashMap<>(readsize + 1);
        targetDataSources.put(DataSourceType.WRITE.getType(), dataSource);
        for (int i = 0; i < readsize; i++) {
            DataSource d = context.getBean("readDataSource" + i, DataSource.class);
            targetDataSources.put(i, d);
        }
        proxy.setDefaultTargetDataSource(dataSource);
        proxy.setTargetDataSources(targetDataSources);
        return proxy;
    }

    @Bean
    @ConditionalOnMissingBean
    public SqlSessionFactoryBean sqlSessionFactory() throws IOException {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(routingDataSouceProxy());
        bean.setVfs(SpringBootVFS.class);
        bean.setTypeAliasesPackage("com.study.wxapp.entity");
//        Resource configResource = new ClassPathResource("/mybatis-config.xml");
//        bean.setConfigLocation(configResource);
        ResourcePatternResolver mapperResource = new PathMatchingResourcePatternResolver();
        bean.setMapperLocations(mapperResource.getResources("classpath*:mapper/*.xml"));
        return bean;
    }

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(routingDataSouceProxy());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (context == null) {
            context = applicationContext;
        }
    }
}