package com.momo.theta;

import com.momo.theta.api.Sequence;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(classes = ThetaExampleSpringBootApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class ThetaSegmentTest {

  @Test
  public void testSegment() {
    // 多线程生成sequence
    for (int i = 0; i < 500; i++) {
      new Thread(() -> {
        //指定
        Sequence sequence = new Sequence("rebateOrderSequence");
        System.out.println(sequence.getSequence());
      }).start();
    }
  }

  @Test
  public void test() {
    //指定
    Sequence sequence = new Sequence("rebateOrderSequence");
    System.out.println(sequence.getSequence());
  }

}
