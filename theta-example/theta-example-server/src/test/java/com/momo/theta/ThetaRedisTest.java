package com.momo.theta;

import com.alibaba.fastjson.JSONObject;
import com.momo.theta.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(classes = ThetaExampleSpringBootApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class ThetaRedisTest {

    @Autowired
    private Cache cache;

    @Test
    public void testAcquireRedission() {
        cache.acquireForRedissonCallable("test", 200L, () -> {
            log.info("执行逻辑");
            return "";

        }, 200L);
    }

    @Test
    public void testSetValue() {
        User user = new User();
        user.setUserId("1");
        user.setUserName("zhubo");
        cache.put("test1", user, 10000);
    }

    @Test
    public void testGetValue() {
        Object test = cache.get("test");
        System.out.println(JSONObject.toJSONString(test));
    }
}
