package com.momo.example.segment;

import com.momo.theta.ThetaExampleSpringBootApplication;
//import com.momo.theta.api.Sequence;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

@SpringBootTest(classes = ThetaExampleSpringBootApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class ThetaSegmentTest {

    @Test
    public void testSegment() {
        // 多线程生成sequence
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                //指定
                //Sequence sequence = new Sequence("commonSequence");
                HashMap<String, String> args = new HashMap<>();
                //System.out.println(sequence.getSequence(args));
            }).start();
        }
    }

    @Test
    public void test() {
        //指定
        //Sequence sequence = new Sequence("commonSequence");
        HashMap<String, String> args = new HashMap<>();
        //System.out.println(sequence.getSequence(args));
    }

}
