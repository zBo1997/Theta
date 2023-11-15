package com.momo.theta.sharding.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadata;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * shardingsphere.shardingjdbc.jdbc.unsupported.AbstractUnsupportedOperationConnection.isValid解决方案
 * 新版集成`sharding-jdbc`时报`SQLFeatureNotSupportedException`异常，该异常的意思是功能不支持的意思，
 * 就是`sharding-jdbc`没有完全实现Datasouce接口规范导致功能不足。
 * 如果是较老一点的Spring可以参考spring boot 集成 sharding jdbc 分库分表 踩坑_taopenglove的博客-CSDN博客文章进行配置，
 * 但是较新版本（2.6.6左右）不支持该方案，但是参考该文章设置的
 * DataSourceHealthContributorAutoConfiguration ，
 * 可以看出最后是根据数据源去拿 DataSourcePoolMetadata 然后进行健康检查。
 * 并且由于 ShardingDataSource 内部是封装了真实数据源的，所以 ShardingDataSource 本身并不需要进行健康检查，
 * 遇到 ShardingDataSource 的情况返回 null 即可（当然如果能全部实现是最好的）。
 */
@Configuration
@Slf4j
public class JpaConfiguration {

    /**
     * 解决新版Spring中,健康健康检查用到 sharding jdbc 时,该组件没有完全实现MySQL驱动导致的问题.
     */
    @Bean
    DataSourcePoolMetadataProvider dataSourcePoolMetadataProvider() {
        return dataSource -> new NotAvailableDataSourcePoolMetadata();
    }

    /**
     * 不可用的数据源池元数据.
     */
    private static class NotAvailableDataSourcePoolMetadata implements DataSourcePoolMetadata {
        @Override
        public Float getUsage() {
            return null;
        }

        @Override
        public Integer getActive() {
            return null;
        }

        @Override
        public Integer getMax() {
            return null;
        }

        @Override
        public Integer getMin() {
            return null;
        }

        @Override
        public String getValidationQuery() {
            log.info("Theta检查数据是否可用");
            // 该字符串是适用于MySQL的简单查询语句,用于检查检查,其他数据库可能需要更换
            return "select 1";
        }

        @Override
        public Boolean getDefaultAutoCommit() {
            return null;
        }
    }
}