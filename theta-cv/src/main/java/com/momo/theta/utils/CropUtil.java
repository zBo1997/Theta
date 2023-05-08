package com.momo.theta.utils;

import com.momo.theta.domain.PlateInfo;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

/**
 * 图像裁剪工具
 */
public class CropUtil {

  /**
   * 根据4个点裁剪图像
   *
   * @param image
   * @param plateBox
   * @return
   */
  public static Mat crop(Mat image, PlateInfo.PlateBox plateBox) {
    Mat endM = null;
    Mat startM = null;
    Mat perspectiveTransform = null;
    try {
      List<Point> dest = new ArrayList<>();
      dest.add(new Point(plateBox.leftTop.x, plateBox.leftTop.y));
      dest.add(new Point(plateBox.rightTop.x, plateBox.rightTop.y));
      dest.add(new Point(plateBox.rightBottom.x, plateBox.rightBottom.y));
      dest.add(new Point(plateBox.leftBottom.x, plateBox.leftBottom.y));
      startM = Converters.vector_Point2f_to_Mat(dest);
      List<Point> ends = new ArrayList<>();
      ends.add(new Point(0, 0));
      ends.add(new Point(plateBox.width(), 0));
      ends.add(new Point(plateBox.width(), plateBox.height()));
      ends.add(new Point(0, plateBox.height()));
      endM = Converters.vector_Point2f_to_Mat(ends);
      perspectiveTransform = Imgproc.getPerspectiveTransform(startM, endM);
      Mat outputMat = new Mat((int) plateBox.height(), (int) plateBox.width(), CvType.CV_8UC4);
      Imgproc.warpPerspective(image, outputMat, perspectiveTransform,
          new Size((int) plateBox.width(), (int) plateBox.height()), Imgproc.INTER_CUBIC);
      return outputMat;
    } finally {
      if (null != endM) {
        endM.release();
      }
      if (null != startM) {
        startM.release();
      }
      if (null != perspectiveTransform) {
        perspectiveTransform.release();
      }
    }
  }

}
