package com.momo.theta.api;

import com.momo.theta.dto.UserInfoDTO;
import com.momo.theta.form.UserForm;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author ZhuBo
 * @Description 订单查询接口
 * @Date 2023/04/12
 * @ClassName RebateOrderService
 */

public interface TestService {


  /**
   * 创建用户
   *
   * @param userForm
   * @return
   */
  @PostMapping("api/user/doCreate")
  UserInfoDTO create(@Validated @RequestBody UserForm userForm);

  /**
   * 查询用户 注意这里的 @RequestParam 必须加如，不然会被解析成POST请求？ 这是为什么，SpringCloud 的Github上也提出了一些解释 详细看下一下这个 issue
   * https://github.com/spring-cloud/spring-cloud-netflix/issues/1253 SpringCloud官方给出解释
   *
   * @param userId 用户编号
   * @return
   */
  @Cacheable(value = "userId", sync = true)
  @GetMapping(value = "/api/user/getUserInfo")
  UserInfoDTO query(@RequestParam String userId);

  /**
   * 测试分布式锁
   *
   * @param userForm
   * @return 返回更新后的结果
   */
  @PostMapping(value = "/api/user/updateUserInfo")
  UserInfoDTO update(@Validated @RequestBody UserForm userForm);


  /**
   * 测试传递图片
   *
   * @param file
   * @return 返回更新后的结果
   */
  @PostMapping(value = "/api/user/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  String uploadFile(@RequestPart("file") MultipartFile file);

  /**
   * 车牌识别的方法
   * @param file 车牌文件
   * @return 车牌识别结果
   */
  @PostMapping(value = "/api/plateRecognition/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  String plateRecognition(@RequestPart("file") MultipartFile file);
}
