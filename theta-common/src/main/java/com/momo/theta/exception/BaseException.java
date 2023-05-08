package com.momo.theta.exception;

import org.apache.commons.lang3.StringUtils;

/**
 * BaseExceptioon
 */
public abstract class BaseException extends RuntimeException {

  protected static final String _CODE = "exception.";
  private static final long serialVersionUID = 4151720706885185333L;
  /**
   * 错误代码
   */
  private String code;

  /**
   * 模块
   */
  private String module;

  /**
   * 错误码对应的参数
   */
  private Object[] args;

  /**
   * 实现给用户展示动态原因, 在message左边追加
   */
  private String leftReason;

  /**
   * 实现给用户展示动态原因, 在message右边追加
   */
  private String rightReason;

  public BaseException(String module, String code) {
    this.module = module;
    this.code = _CODE + code;
  }

  public BaseException(String module, String code, String message) {
    super(message);
    this.module = module;
    this.module = module;
    this.module = module;
    this.code = _CODE + code;
  }

  public BaseException(String module, String code, String message, Throwable cause) {
    super(message, cause);
    this.module = module;
    this.code = _CODE + code;
  }

  public BaseException(String module, String code, String message, Throwable cause, Object[] args) {
    super(message, cause);
    this.module = module;
    this.code = _CODE + code;
    this.args = args;
  }

  public Object[] getArgs() {
    return args;
  }

  public void setArgs(Object[] args) {
    this.args = args;
  }

  public String getLeftReason() {
    return leftReason;
  }

  public BaseException setLeftReason(String leftReason) {
    this.leftReason = leftReason;
    this.rightReason = rightReason;
    return this;
  }

  public String getRightReason() {
    return rightReason;
  }

  public String getCode() {
    return code;
  }

  public abstract String getSuperCode();

  @Override
  public String toString() {
    return new StringBuilder().append(
        "Exception:[" + this.getClass().getName() + "],module:[" + StringUtils.defaultString(module)
            + "],code:[" + StringUtils.defaultString(code) + "],message:["
            + StringUtils.defaultString(getMessage()) + "],args:[" + StringUtils.join(args, ",")
            + "]").toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    if (obj instanceof BaseException) {
      BaseException other = (BaseException) obj;
      return this.getCode().equals(other.getCode());
    }
    return false;
  }
}