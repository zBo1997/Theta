package com.momo.example.segment;

import com.momo.theta.example.ThetaExampleSpringBootApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = ThetaExampleSpringBootApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@Slf4j
public class ThetaSegmentTest {

    @Test
    public void testSegment() {
        // 多线程生成sequence
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                //指定
                Sequence sequence = new Sequence("myCompositeString");
                HashMap<String, String> args = new HashMap<>();
                //args.put("customerString", "100");
                System.out.println(sequence.getSequence(args));
            }).start();
        }
        log.info("I am test");

    }


}
