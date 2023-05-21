package com.momo.theta;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.momo.theta.enums.ExceptionCodeEnum;
import com.momo.theta.exception.BizException;
import java.util.HashMap;
import java.util.Map;


/**
 * @author zhubo
 */
@SuppressWarnings("ALL")
public class Result<T> {
    public static final String DEF_ERROR_MESSAGE = "系统繁忙，请稍候再试";
    public static final String HYSTRIX_ERROR_MESSAGE = "请求超时，请稍候再试";
    public static final String SUCCESS_CODE = "200";
    public static final String FAIL_CODE = "999";
    public static final String TIMEOUT_CODE = "998";
    /**
     * 统一参数验证异常
     */
    public static final String VALID_EX_CODE = "997";
    public static final String OPERATION_EX_CODE = "996";
    /**
     * 调用是否成功标识，0：成功，-1:系统繁忙，此时请开发者稍候再试 详情见[ExceptionCode]
     */
    private String code;

    /**
     * 是否执行默认操作
     */
    @JsonIgnore
    private Boolean defExec = true;

    /**
     * 调用结果
     */
    private T data;

    /**
     * 结果消息，如果调用成功，消息通常为空T
     */
    private String msg = "ok";

    private String path;

    /**
     * 附加数据
     */
    private Map<Object, Object> extra;

    /**
     * 响应时间
     */
    private long timestamp = System.currentTimeMillis();

    /**
     * 系统报错时，抛出的原生信息
     */
    private String errorMsg = "";

    private Result() {
        this.defExec = false;
        this.timestamp = System.currentTimeMillis();
    }

    public Result(String code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.defExec = false;
        this.timestamp = System.currentTimeMillis();
    }

    public Result(String code, T data, String msg, String errorMsg) {
        this(code, data, msg);
        this.errorMsg = errorMsg;
    }

    public Result(String code, T data, String msg, boolean defExec) {
        this(code, data, msg);
        this.defExec = defExec;
    }

    public static <E> Result<E> result(String code, E data, String msg) {
        return new Result<>(code, data, msg);
    }

    public static <E> Result<E> result(String code, E data, String msg, String errorMsg) {
        return new Result<>(code, data, msg, errorMsg);
    }

    /**
     * 请求成功消息
     *
     * @param data 结果
     * @return RPC调用结果
     */
    public static <E> Result<E> success(E data) {
        return new Result<>(SUCCESS_CODE, data, "ok");
    }

    public static Result<Boolean> success() {
        return new Result<>(SUCCESS_CODE, true, "ok");
    }


    public static <E> Result<E> successDef(E data) {
        return new Result<E>(SUCCESS_CODE, data, "ok", true);
    }

    public static <E> Result<E> successDef() {
        return new Result<>(SUCCESS_CODE, null, "ok", true);
    }

    public static <E> Result<E> successDef(E data, String msg) {
        return new Result<>(SUCCESS_CODE, data, msg, true);
    }

    /**
     * 请求成功方法 ，data返回值，msg提示信息
     *
     * @param data 结果
     * @param msg  消息
     * @return RPC调用结果
     */
    public static <E> Result<E> success(E data, String msg) {
        return new Result<>(SUCCESS_CODE, data, msg);
    }

    /**
     * 请求失败消息
     *
     * @param msg
     * @return
     */
    public static <E> Result<E> fail(String code, String msg) {
        return new Result<>(code, null, (msg == null || msg.isEmpty()) ? DEF_ERROR_MESSAGE : msg);
    }

    public static <E> Result<E> fail(String code, String msg, String errorMsg) {
        return new Result<>(code, null, (msg == null || msg.isEmpty()) ? DEF_ERROR_MESSAGE : msg, errorMsg);
    }

    public static <E> Result<E> fail(String msg) {
        return fail(OPERATION_EX_CODE, msg);
    }

    public static <E> Result<E> fail(String msg, Object... args) {
        String message = (msg == null || msg.isEmpty()) ? DEF_ERROR_MESSAGE : msg;
        return new Result<>(OPERATION_EX_CODE, null, String.format(message, args));
    }

    public static <E> Result<E> fail(ExceptionCodeEnum exceptionCodeEnum) {
        return validFail(exceptionCodeEnum);
    }

    public static <E> Result<E> fail(BizException exception) {
        if (exception == null) {
            return fail(DEF_ERROR_MESSAGE);
        }
        return new Result<>(exception.getCode(), null, exception.getMessage(), exception.getMessage());
    }

    /**
     * 请求失败消息，根据异常类型，获取不同的提供消息
     *
     * @param throwable 异常
     * @return RPC调用结果
     */
    public static <E> Result<E> fail(Throwable throwable) {
        String msg = throwable != null ? throwable.getMessage() : DEF_ERROR_MESSAGE;
        return fail(FAIL_CODE, msg, msg);
    }

    public static <E> Result<E> validFail(String msg) {
        return new Result<>(VALID_EX_CODE, null, (msg == null || msg.isEmpty()) ? DEF_ERROR_MESSAGE : msg);
    }

    public static <E> Result<E> validFail(String msg, Object... args) {
        String message = (msg == null || msg.isEmpty()) ? DEF_ERROR_MESSAGE : msg;
        return new Result<>(VALID_EX_CODE, null, String.format(message, args));
    }

    public static <E> Result<E> validFail(ExceptionCodeEnum exceptionCodeEnum) {
        return new Result<>(exceptionCodeEnum.getCode(), null,
                (exceptionCodeEnum.getMsg() == null || exceptionCodeEnum.getMsg().isEmpty()) ? DEF_ERROR_MESSAGE : exceptionCodeEnum.getMsg());
    }

    public static <E> Result<E> timeout() {
        return fail(TIMEOUT_CODE, HYSTRIX_ERROR_MESSAGE);
    }


    public Result<T> put(String key, Object value) {
        if (this.extra == null) {
            this.extra = new HashMap<>(16);
        }
        this.extra.put(key, value);
        return this;
    }

    public Result<T> putAll(Map<Object, Object> extra) {
        if (this.extra == null) {
            this.extra = new HashMap<>(16);
        }
        this.extra.putAll(extra);
        return this;
    }

    /**
     * 逻辑处理是否成功
     *
     * @return 是否成功
     */
    public Boolean getIsSuccess() {
        return this.code == SUCCESS_CODE || this.code == "";
    }

    public String getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
