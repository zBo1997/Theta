package com.momo.theta.advice;


import com.alibaba.fastjson.JSONObject;
import com.momo.theta.Result;
import com.momo.theta.enums.ExceptionCodeEnum;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 *
 * @author zhubo
 */
@RestControllerAdvice
public class ThetaGlobalExceptionHandler {


  private static final Logger log = LoggerFactory.getLogger(ThetaGlobalExceptionHandler.class);


  /**
   * 全局异常处理处理数据参数校验异常
   *
   * @param exception 校验错误信息
   * @return Result 返回统一结果集
   */
  @ExceptionHandler(Exception.class)
  public Result<?> handleException(Exception exception) {
    log.error(exception.getMessage(), exception);
    //只返回异常中的一个
    return Result.fail(ExceptionCodeEnum.SYSTEM_BUSY);
  }


  /**
   * 全局异常处理处理数据参数校验异常
   *
   * @param exception 校验错误信息
   * @return Result 返回统一结果集
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
    log.error(exception.getMessage(), exception);
    //获取所有的异常
    List<ObjectError> allErrors = exception.getBindingResult().getAllErrors();
    //只返回异常中的一个
    return Result.fail(JSONObject.toJSONString(allErrors.get(0).getDefaultMessage()));
  }


}
