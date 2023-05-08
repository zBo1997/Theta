package com.momo.theta.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Objects;
import javax.imageio.ImageIO;
import org.opencv.core.Mat;
import org.opencv.core.Range;

public class MatUtil {

  /**
   * 将Mat转换为BufferedImage
   *
   * @param mat
   * @return BufferedImage
   */
  public static BufferedImage matToBufferedImage(Mat mat) {
    int dataSize = mat.cols() * mat.rows() * (int) mat.elemSize();
    byte[] data = new byte[dataSize];
    mat.get(0, 0, data);
    int type = mat.channels() == 1 ? BufferedImage.TYPE_BYTE_GRAY : BufferedImage.TYPE_3BYTE_BGR;
    if (type == BufferedImage.TYPE_3BYTE_BGR) {
      for (int i = 0; i < dataSize; i += 3) {
        byte blue = data[i + 0];
        data[i + 0] = data[i + 2];
        data[i + 2] = blue;
      }
    }
    BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
    image.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data);
    return image;
  }

  /**
   * 将Mat转换为 Base64
   *
   * @param mat
   * @return Base64
   */
  public static String matToBase64(Mat mat) {
    ByteArrayOutputStream byteArrayOutputStream = null;
    try {
      byteArrayOutputStream = new ByteArrayOutputStream();
      ImageIO.write(matToBufferedImage(mat), "jpg", byteArrayOutputStream);
      byte[] bytes = byteArrayOutputStream.toByteArray();
      Base64.Encoder encoder = Base64.getMimeEncoder();
      return encoder.encodeToString(Objects.requireNonNull(bytes));
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      if (null != byteArrayOutputStream) {
        try {
          byteArrayOutputStream.close();
        } catch (Exception e) {
        }
      }
    }
  }

  /**
   * 横向拼接两个图像的数据（Mat），该两个图像的类型必须是相同的类型，如：均为CvType.CV_8UC3类型
   *
   * @param m1 要合并的图像1（左图）
   * @param m2 要合并的图像2（右图）
   * @return 拼接好的Mat图像数据集。其高度等于两个图像中高度较大者的高度；其宽度等于两个图像的宽度之和。类型与两个输入图像相同。
   * @throws Exception 当两个图像数据的类型不同时，抛出异常
   * @author bailichun
   * @since 2020.02.20 15:00
   */
  public static Mat concat(Mat m1, Mat m2) {
    if (m1.type() != m2.type()) {
      throw new RuntimeException("concat:两个图像数据的类型不同！");
    }
    long time = System.currentTimeMillis();
    //宽度为两图的宽度之和
    double w = m1.size().width + m2.size().width;
    //高度取两个矩阵中的较大者的高度
    double h = m1.size().height > m2.size().height ? m1.size().height : m2.size().height;
    //创建一个大矩阵对象
    Mat des = Mat.zeros((int) h, (int) w, m1.type());
    //在最终的大图上标记一块区域，用于存放复制图1（左图）的数据，大小为从第0列到m1.cols()列
    Mat rectForM1 = des.colRange(new Range(0, m1.cols()));
    //标记出位于rectForM1的垂直方向上中间位置的区域，高度为图1的高度，此时该区域的大小已经和图1的大小相同。（用于存放复制图1（左图）的数据）
    int rowOffset1 = (int) (rectForM1.size().height - m1.rows()) / 2;
    rectForM1 = rectForM1.rowRange(rowOffset1, rowOffset1 + m1.rows());
    //在最终的大图上标记一块区域，用于存放复制图2（右图）的数据
    Mat rectForM2 = des.colRange(new Range(m1.cols(), des.cols()));
    //标记出位于rectForM2的垂直方向上中间位置的区域，高度为图2的高度，此时该区域的大小已经和图2的大小相同。（用于存放复制图2（右图）的数据）
    int rowOffset2 = (int) (rectForM2.size().height - m2.rows()) / 2;
    rectForM2 = rectForM2.rowRange(rowOffset2, rowOffset2 + m2.rows());
    //将图1拷贝到des的指定区域 rectForM1
    m1.copyTo(rectForM1);
    //将图2拷贝到des的指定区域 rectForM2
    m2.copyTo(rectForM2);
    return des;
  }

}
