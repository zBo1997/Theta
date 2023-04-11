package com.momo.theta;

/**
 * 事务支持处理
 */
@FunctionalInterface
public interface TransactionSupport {

    /**
     * 执行方法
     */
    void process();
}
