package com.momo.theta.controller;

import com.momo.theta.feign.OcrFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("ocrTest")
public class OcrTestController {

    @Autowired
    private OcrFeign ocrFeign;

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
    String uploadImage(@RequestPart("file") MultipartFile file){
        return file.getOriginalFilename();
    }
}
