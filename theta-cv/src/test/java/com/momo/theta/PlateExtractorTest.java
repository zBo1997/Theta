package com.momo.theta;

import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;
import com.momo.theta.model.DrawImage;
import com.momo.theta.model.ExtParam;
import com.momo.theta.model.ImageMat;
import com.momo.theta.model.PlateImage;
import com.momo.theta.model.PlateInfo;
import com.momo.theta.extract.PlateExtractor;
import com.momo.theta.extract.PlateExtractorImpl;
import com.momo.theta.models.TorchPlateDetection;
import com.momo.theta.models.TorchPlateRecognition;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

@Slf4j
public class PlateExtractorTest extends BaseTest {

  private static String plateDetectionPath = "theta-cv/src/main/resources/models/plate_detect.onnx";
  private static String plateRecognitionPath = "theta-cv/src/main/resources/models/plate_rec_color.onnx";

  public static void main(String[] args) throws IOException {
    File file = new File(
        "C:\\Users\\zhubo\\IdeaProjects\\open-anpr\\open-anpr-core\\src\\main\\resources\\models\\plate_detect.onnx");
    File filePlate = new File(
        "C:\\Users\\zhubo\\IdeaProjects\\open-anpr\\open-anpr-core\\src\\main\\resources\\models\\plate_rec_color.onnx");
    FileInputStream fis = new FileInputStream(file);
    FileInputStream fisPlate = new FileInputStream(filePlate);
    byte[] bytes = new byte[(int) file.length()];
    byte[] bytesPlate = new byte[(int) filePlate.length()];
    fis.read(bytes);
    fisPlate.read(bytesPlate);
    TorchPlateDetection torchPlateDetection = new TorchPlateDetection(bytes, 1,
        OrtEnvironment.getEnvironment(),new OrtSession.SessionOptions());
    TorchPlateRecognition torchPlateRecognition = new TorchPlateRecognition(bytesPlate,
        1,OrtEnvironment.getEnvironment(),new OrtSession.SessionOptions());
    PlateExtractor extractor = new PlateExtractorImpl(torchPlateDetection, torchPlateRecognition);

    String imagePath = "open-anpr-core/src/test/resources/images";
    Map<String, String> map = getImagePathMap(imagePath);
    for (String fileName : map.keySet()) {
      String imageFilePath = map.get(fileName);
      System.out.println(imageFilePath);
      Mat image = Imgcodecs.imread(imageFilePath);
      long startTime = System.currentTimeMillis();
      ExtParam extParam = ExtParam.build()
          .setTopK(20)
          .setScoreTh(0.3f)
          .setIouTh(0.5f);
      //推理
      PlateImage plateImage = extractor.extract(ImageMat.fromCVMat(image), extParam,
          new HashMap<>());
      System.out.println("cost:" + (System.currentTimeMillis() - startTime));
      List<PlateInfo> plateInfos = plateImage.PlateInfos();
      //可视化
      DrawImage drawImage = DrawImage.build(imageFilePath);
      for (PlateInfo plateInfo : plateInfos) {
        //画框
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
        //添加文本
        PlateInfo.ParseInfo parseInfo = plateInfo.parseInfo;
        log.info("车牌号为：{}", parseInfo.plateNo);
        int fonSize = Float.valueOf(plateInfo.box.width() / parseInfo.plateNo.length() * 1.4f)
            .intValue();
        drawImage.drawText(parseInfo.plateNo,
            new DrawImage.Point((int) points[0].x, (int) points[0].y - (int) (fonSize * 2.2)),
            fonSize, Color.RED);
        drawImage.drawText((plateInfo.single ? "单排" : "双排") + ":" + parseInfo.plateColor,
            new DrawImage.Point((int) points[0].x, (int) points[0].y - (int) (fonSize * 1.2)),
            fonSize, Color.RED);
      }
      //show
      ImageMat.fromCVMat(drawImage.toMat()).imShow();
      fis.close();
      fisPlate.close();
      image.release();
    }
  }
}
