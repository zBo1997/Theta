package com.momo.theta;

import com.momo.theta.exception.TimeoutException;

public interface Cache {


  /**
   * 提供一个单纯的acquireForRedisson方法,新业务可以使用
   *
   * @param lockKey  锁Key
   * @param expire   过期时间
   * @param callback 回调函数
   * @param timeout  超时时间
   * @param <T>      回调返回的类型
   * @return
   * @throws TimeoutException
   */
  <T> T acquireForRedissonCallable(final String lockKey, final Long expire,
      final LockCallback<T> callback, final Long timeout) throws TimeoutException;

  /**
   * 添加使用默认延迟延迟时间
   *
   * @param key
   * @param value
   */
  void put(Object key, Object value);

  /**
   * 添加 带有延迟时间
   *
   * @param key    key 值
   * @param value  入参
   * @param expire 超时时间(s）
   */
  void put(Object key, Object value, long expire);


  /**
   * 返回对应的Key
   *
   * @param key key 值
   * @return data
   */
  Object get(Object key);


  /**
   * 移除key
   *
   * @param key 值
   * @return 删除对应的数据
   */
  Object remove(Object key);


}