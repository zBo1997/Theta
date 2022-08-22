package com.momo.theta.segment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;

/**
 * @author momo
 * @since 2022/02/23/09:35 上午
 */
@SpringBootTest(classes = TestSegmentService.class)
@ComponentScan("com.momo.theta.segment.config")
@TestPropertySource(locations = {
        "classpath:segment.yaml"})
public class TestSegmentService {

    @Autowired
    private Environment environment;


    @Test
    public void test() {
        Environment environment = this.environment;
        System.out.println(environment);
    }
}
