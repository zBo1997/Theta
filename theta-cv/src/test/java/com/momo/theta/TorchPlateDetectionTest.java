package com.momo.theta;

import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;
import cn.hutool.core.io.FileUtil;
import com.momo.theta.model.DrawImage;
import com.momo.theta.model.ImageMat;
import com.momo.theta.model.PlateInfo;
import com.momo.theta.models.TorchPlateDetection;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;

public class TorchPlateDetectionTest {

  public static void main(String[] args) {
    TorchPlateDetection torchPlateDetection = new TorchPlateDetection(
        FileUtil.readBytes("C:\\Users\\zhubo\\IdeaProjects\\Theta\\theta-cv\\src\\main\\resources\\models\\plate_detect.onnx"), 1, OrtEnvironment.getEnvironment(),
        new OrtSession.SessionOptions());

    String imagePath = "theta-cv/src/test/resources/images/image002.jpg";
    ImageMat imageMat = ImageMat.fromImage(imagePath);
    List<PlateInfo> plateInfos = torchPlateDetection.inference(imageMat, 0.3f, 0.5f,
        new HashMap<>());
    System.out.println(plateInfos);

    DrawImage drawImage = DrawImage.build(imagePath);
    for (PlateInfo plateInfo : plateInfos) {
      PlateInfo.Point[] points = plateInfo.box.toArray();
      for (int i = 0; i < points.length; i++) {
        if (i + 1 == points.length) {
          drawImage.drawLine(
              new DrawImage.Point((int) points[i].x, (int) points[i].y),
              new DrawImage.Point((int) points[0].x, (int) points[0].y),
              2, Color.RED
          );
        } else {
          drawImage.drawLine(
              new DrawImage.Point((int) points[i].x, (int) points[i].y),
              new DrawImage.Point((int) points[i + 1].x, (int) points[i + 1].y),
              2, Color.RED
          );
        }
      }
    }
    ImageMat.fromCVMat(drawImage.toMat()).imShow();
  }

}
