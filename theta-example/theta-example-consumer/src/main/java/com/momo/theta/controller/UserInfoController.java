package com.momo.theta.controller;

import com.alibaba.fastjson.JSONObject;
import com.momo.theta.feign.UserFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("userInfo")
public class UserInfoController {

    @Autowired
    private UserFeign userFeign;

    /**
     * 测试查询
     */
    @GetMapping("query")
    public String query(String userId) {
        return JSONObject.toJSONString(userFeign.query(userId));
    }

}