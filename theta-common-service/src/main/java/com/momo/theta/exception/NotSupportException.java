package com.momo.theta.exception;

import org.apache.commons.lang3.StringUtils;

/**
 * 不支持异常
 * @author zhubo
 */
public class NotSupportException extends BaseException {

  private static final long serialVersionUID = 1183890068898130668L;

  protected static final String _CODE = "not.support.";

  protected static final String DOT = ".";


  public NotSupportException(String module, String code) {
    super(module, _CODE + code);
  }

  public NotSupportException(String module, String code, String message) {
    super(module, _CODE + code, message);
  }

  public NotSupportException(String module, String code, String message, Throwable cause) {
    super(module, _CODE + code, message, cause);
  }

  public NotSupportException(String module, String code, String message, Throwable cause,
      Object[] args) {
    super(module, _CODE + code, message, cause, args);
  }

  @Override
  public String getSuperCode() {
    String code = BaseException._CODE + _CODE;
    return StringUtils.substringBeforeLast(code, DOT);
  }
}