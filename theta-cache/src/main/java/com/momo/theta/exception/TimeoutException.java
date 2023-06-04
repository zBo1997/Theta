package com.momo.theta.exception;

import org.apache.commons.lang3.StringUtils;

/**
 * redisTimeOutException
 * @author zhubo
 */
public class TimeoutException extends BaseException {

  protected static final String _CODE = "timeout.";
  protected static final String DOT = ".";
  private static final long serialVersionUID = 1982790068898130668L;

  public TimeoutException(String module, String code) {
    super(module, _CODE + code);
  }

  public TimeoutException(String module, String code, String message) {
    super(module, _CODE + code, message);
  }

  public TimeoutException(String module, String code, String message, Throwable cause) {
    super(module, _CODE + code, message, cause);
  }

  public TimeoutException(String module, String code, String message, Throwable cause,
      Object[] args) {
    super(module, _CODE + code, message, cause, args);
  }

  @Override
  public String getSuperCode() {
    String code = BaseException._CODE + _CODE;
    return StringUtils.substringBeforeLast(code, DOT);
  }
}