package com.momo.theta.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Stopwatch;
import com.momo.theta.entity.User;
import com.momo.theta.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    UserMapper userMapper;


    /**
     * 这是一个业务处理的请求
     */
    @GetMapping("gcTest")
    public void queueNormalDelay() {
        //创建一个监听器
        Stopwatch started = Stopwatch.createStarted();
        Int sum = new Int();
        for (int i = 0; i <Integer.MAX_VALUE; i++) {
            sum.add(1);
        }
        started.stop();
        log.info("waste time :{}", JSONObject.toJSONString(started.toString()));
    }

    class Int {
        public int val = 0;

        public void add(int delta) {
            val += delta;
        }
    }


    /**
     * 这是一个业务处理的请求
     */
    @GetMapping("test")
    public void test() {
        User user = userMapper.selectById(1);
        log.info("waste time :{}", JSONObject.toJSONString(user));
    }

}
