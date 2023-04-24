package com.momo.theta.controller;

import com.momo.theta.feign.OcrFeign;
import com.momo.theta.feign.UserFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("ocrTest")
public class OcrTestController {

    @Autowired
    private OcrFeign ocrFeign;

    @Autowired
    private UserFeign userFeign;

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
     * @param file
     * @return
     */
    @PostMapping(value = "/tbk/feedback/upload")
    String uploadImage(@RequestParam("file") MultipartFile file){
        log.info("传输文件的名字：{}",file.getOriginalFilename());
        return ocrFeign.ocr(file);
    }

}
