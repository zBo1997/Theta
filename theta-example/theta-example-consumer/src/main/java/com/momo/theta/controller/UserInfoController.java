package com.momo.theta.controller;

import com.alibaba.fastjson.JSONObject;
import com.momo.theta.feign.TestFeign;
import com.momo.theta.form.UserForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("userInfo")
public class UserInfoController {

  @Autowired
  private TestFeign testFeign;

  /**
   * 测试查询
   */
  @GetMapping("query")
  public String query(@RequestParam String userId) {
    return JSONObject.toJSONString(testFeign.query(userId));
  }


  /**
   * 测试查询
   */
  @PostMapping("update")
  public String update(@RequestBody UserForm userForm) {
    log.info("consumer update form ：{}", JSONObject.toJSONString(userForm));
    return JSONObject.toJSONString(testFeign.update(userForm));
  }


}
