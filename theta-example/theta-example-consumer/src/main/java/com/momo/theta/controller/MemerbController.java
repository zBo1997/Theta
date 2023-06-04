package com.momo.theta.controller;

import com.alibaba.fastjson.JSONObject;
import com.momo.theta.Result;
import com.momo.theta.feign.MemberFeignApi;
import com.momo.theta.form.UserSaveForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/member")
public class MemerbController {

  @Autowired
  private MemberFeignApi memberFeign;


  /**
   * 创建请求
   */
  @PostMapping("/create")
  public String create(@RequestBody UserSaveForm userForm) {
    log.info("consumer update form ：{}", JSONObject.toJSONString(userForm));
    Result<?> object = memberFeign.create(userForm);
    log.info("consumer result ：{}", JSONObject.toJSONString(object));
    return JSONObject.toJSONString(object);
  }
  

}
