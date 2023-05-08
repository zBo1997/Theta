package com.momo.theta;

/**
 * 回调执行方法接口
 */
@FunctionalInterface
public interface LockCallback<T> {

  /**
   * 回调执行的方法
   *
   * @return 返回指定类型
   */
  T callBack();
}
