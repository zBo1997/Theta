package com.momo.theta.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

public interface OcrService {

    @GetMapping("/simulation/analysis?name=zhubo")
    String demoTest();

    @PostMapping("/ocr/test")
    void ocr(@RequestPart("file") MultipartFile file);
}
