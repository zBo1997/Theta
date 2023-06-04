package com.momo.theta.models;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;
import ai.onnxruntime.OrtSession.SessionOptions;
import com.momo.theta.base.BaseOnnxInfer;
import com.momo.theta.base.PlateRecognition;
import com.momo.theta.model.ImageMat;
import com.momo.theta.model.PlateInfo.ParseInfo;
import com.momo.theta.utils.ArrayUtil;
import com.momo.theta.utils.MatUtil;
import com.momo.theta.utils.ReleaseUtil;
import com.momo.theta.utils.SoftMaxUtil;
import java.util.Collections;
import java.util.Map;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * 车牌识别 git: https://github.com/we0091234/crnn_plate_recognition/tree/plate_color
 */
public class TorchPlateRecognition extends BaseOnnxInfer implements PlateRecognition {

  private final static String[] PLATE_COLOR = new String[]{"黑色", "蓝色", "绿色", "白色", "黄色"};
  private final static String PLATE_NAME =
      "#京沪津渝冀晋蒙辽吉黑苏浙皖闽赣鲁豫鄂湘粤桂琼川贵云藏陕甘青宁新" +
          "学警港澳挂使领民航危0123456789ABCDEFGHJKLMNPQRSTUVWXYZ险品";
  private static float STD_VALUE = 0.193f;
  private static float MEAN_VALUE = 0.588f * 255;

  public TorchPlateRecognition(byte[] modelPath, int threads, OrtEnvironment ortEnvironment,
      SessionOptions session) {
    super(modelPath, threads, ortEnvironment, session);
  }

  private static ImageMat splitAndMergePlate(ImageMat plate) {
    Mat upperImageTemp = null, upperImage = null;
    Mat lowerImageTemp = null, lowerImage = null;
    Mat upperReSize = null;
    try {
      int width = plate.getWidth();
      int height = plate.getHeight();
      //上半部分
      int upperSplit = Double.valueOf(5.0f / 12 * height).intValue();
      Rect upperRect = new Rect(0, 0, width, upperSplit);
      upperImageTemp = new Mat(plate.toCvMat(), upperRect);
      upperImage = new Mat();
      upperImageTemp.copyTo(upperImage);
      //下半部分
      int lowerSplit = Double.valueOf(1.0f / 3 * height).intValue();
      Rect lowerRect = new Rect(0, lowerSplit, width, height - lowerSplit);
      lowerImageTemp = new Mat(plate.toCvMat(), lowerRect);
      lowerImage = new Mat();
      lowerImageTemp.copyTo(lowerImage);
      //合并图像
      upperReSize = new Mat();
      int heightSize = height - lowerSplit;
      Imgproc.resize(upperImage, upperReSize, new Size(width, heightSize), 0, 0,
          Imgproc.INTER_AREA);
      Mat concatMat = MatUtil.concat(upperReSize, lowerImage);
      //返回合并后的图像
      return ImageMat.fromCVMat(concatMat);
    } finally {
      ReleaseUtil.release(upperImageTemp, upperImage);
      ReleaseUtil.release(lowerImageTemp, lowerImage);
      ReleaseUtil.release(upperReSize);
    }
  }

  private static int[] maxScoreIndex(float[][] result) {
    int[] indexes = new int[result.length];
    for (int i = 0; i < result.length; i++) {
      int index = 0;
      float max = Float.MIN_VALUE;
      for (int j = 0; j < result[i].length; j++) {
        if (max < result[i][j]) {
          max = result[i][j];
          index = j;
        }
      }
      indexes[i] = index;
    }
    return indexes;
  }

  private static String decodePlate(int[] indexes) {
    int pre = 0;
    StringBuffer sb = new StringBuffer();
    for (int index : indexes) {
      if (index != 0 && pre != index) {
        sb.append(PLATE_NAME.charAt(index));
      }
      pre = index;
    }
    return sb.toString();
  }

  private static Double[] decodeColor(double[] indexes) {
    double index = -1;
    double max = Double.MIN_VALUE;
    for (int i = 0; i < indexes.length; i++) {
      if (max < indexes[i]) {
        max = indexes[i];
        index = i;
      }
    }
    return new Double[]{index, max};
  }

  @Override
  public ParseInfo inference(ImageMat image, Boolean single, Map<String, Object> params) {
    OnnxTensor tensor = null;
    OrtSession.Result output = null;
    ImageMat imageMat = null;
    try {
      //图片处理
      imageMat = null == single || single ? image.clone() : splitAndMergePlate(image);
      //转换数据为张量
      tensor = imageMat.resizeAndNoReleaseMat(168, 48)
          .blobFromImageAndDoReleaseMat(1.0 / 255, new Scalar(MEAN_VALUE, MEAN_VALUE, MEAN_VALUE),
              false)
          .to4dFloatOnnxTensorAndNoReleaseMat(new float[]{STD_VALUE, STD_VALUE, STD_VALUE}, true);
      //ONNX推理
      output = getSession().run(Collections.singletonMap(getInputName(), tensor));
      //车牌识别
      float[][][] result = (float[][][]) output.get(0).getValue();
      String plateNo = decodePlate(maxScoreIndex(result[0]));
      //车牌颜色识别
      float[][] color = (float[][]) output.get(1).getValue();
      double[] colorSoftMax = SoftMaxUtil.softMax(ArrayUtil.floatToDouble(color[0]));
      Double[] colorRResult = decodeColor(colorSoftMax);
      //返回解析到的数据
      return ParseInfo.build(
          image.toBase64AndNoReleaseMat(), plateNo,
          PLATE_COLOR[colorRResult[0].intValue()],
          colorRResult[1].floatValue()
      );
    } catch (Exception e) {
      //抛出异常
      throw new RuntimeException(e);
    } finally {
      //释放资源
      if (null != tensor) {
        ReleaseUtil.release(tensor);
      }
      if (null != output) {
        ReleaseUtil.release(output);
      }
      if (null != imageMat) {
        ReleaseUtil.release(imageMat);
      }
    }
  }
}
