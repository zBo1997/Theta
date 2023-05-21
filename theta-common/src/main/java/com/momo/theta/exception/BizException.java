package com.momo.theta.exception;

import com.momo.theta.enums.ExceptionCodeEnum;

/**
 * 业务异常
 * 用于在处理业务逻辑时，进行抛出的异常。
 *
 * @author zuihou
 * @version 1.0
 */
public class BizException extends BaseException {

    private static final long serialVersionUID = -3843907364558373817L;

    public BizException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getSuperCode() {
        return super.getCode();
    }

    public BizException(String message) {
        super(ExceptionCodeEnum.SYSTEM_BUSY.getCode(), message);
    }

    public BizException(String message, Throwable cause) {
        super(ExceptionCodeEnum.SYSTEM_BUSY.getCode(), message, cause);
    }

    public BizException(String code, String message) {
        super(code, message);
    }

    public BizException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public BizException(String code, String message, Object... args) {
        super(code, message, args);
    }

    /**
     * 实例化异常
     *
     * @param code    自定义异常编码
     * @param message 自定义异常消息
     * @param args    已定义异常参数
     * @return 异常实例
     */
    public static BizException wrap(String code, String message, Object... args) {
        return new BizException(code, message, args);
    }

    public static BizException wrap(String message, Object... args) {
        return new BizException(ExceptionCodeEnum.SYSTEM_BUSY.getCode(), message, args);
    }

    public static BizException validFail(String message, Object... args) {
        return new BizException(ExceptionCodeEnum.BASE_VALID_PARAM.getCode(), message, args);
    }

    public static BizException wrap(ExceptionCodeEnum ex) {
        return new BizException(ex.getCode(), ex.getMsg());
    }

    public static BizException wrap(ExceptionCodeEnum ex, Throwable cause) {
        return new BizException(ex.getCode(), ex.getMsg(), cause);
    }

    @Override
    public String toString() {
        return "BizException [message=" + getMessage() + ", code=" + getCode() + "]";
    }

}
