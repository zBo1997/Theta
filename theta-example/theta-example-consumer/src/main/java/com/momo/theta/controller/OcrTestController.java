package com.momo.theta.controller;

import com.momo.theta.feign.OcrFeign;
import com.momo.theta.feign.TestFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("ocrTest")
public class OcrTestController {

  @Autowired
  private OcrFeign ocrFeign;

  @Autowired
  private TestFeign testFeign;

  /**
   * 测试查询
   */
  @GetMapping("query")
  public String query() {
    return ocrFeign.demoTest();
  }


  /**
   * 上传图片文件
   *
   * @param file 文件
   * @return 上传的图片
   */
  @PostMapping(value = "/tbk/feedback/upload")
  public String uploadImage(@RequestParam("file") MultipartFile file) {
    log.info("传输文件的名字：{}", file.getOriginalFilename());
    return ocrFeign.ocr(file);
  }


  /**
   * 车牌图片识别
   *
   * @param file
   * @return 识别结果
   */
  @PostMapping(value = "/tbk/feedback/plate")
  public String plate(@RequestParam("file") MultipartFile file) {
    log.info("传输文件的名字：{}", file.getOriginalFilename());
    return testFeign.plateRecognition(file);
  }

}
