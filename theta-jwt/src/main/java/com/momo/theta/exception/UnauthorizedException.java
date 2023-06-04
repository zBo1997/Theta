package com.momo.theta.exception;


import com.momo.theta.enums.ExceptionCodeEnum;

/**
 * 401 未认证 未登录
 *
 * @author zhubo
 * @version 1.0
 */
public class UnauthorizedException extends BaseException {

  protected static final String _CODE = "unauthorizedException.";

  private static final long serialVersionUID = 1L;

  public UnauthorizedException(String msg) {
    super(msg);
  }

  @Override
  public String getSuperCode() {
    return null;
  }

  public UnauthorizedException(String code, String message, Throwable cause) {
    super(code, message, cause);
  }

  public UnauthorizedException(String model, String code, String message) {
    super(model, code, message);
  }

  public static UnauthorizedException wrap(String model, String msg) {
    return new UnauthorizedException(model, ExceptionCodeEnum.UNAUTHORIZED.getCode(), msg);
  }


  public static UnauthorizedException wrap(String msg) {
    return new UnauthorizedException(msg);
  }

  @Override
  public String toString() {
    return "UnauthorizedException [message=" + getMessage() + ", code=" + getCode() + "]";
  }

}
