package com.momo.theta.utils;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * 图像对齐工具
 */
public class AlignUtil {


  /**
   * 对齐
   *
   * @param image      图像数据
   * @param imagePoint 图像中的关键点
   * @param stdWidth   定义的标准图像的宽度
   * @param stdHeight  定义的标准图像的高度
   * @param stdPoint   定义的标准关键点
   */
  public static Mat alignedImage(Mat image, double[][] imagePoint, int stdWidth, int stdHeight,
      double[][] stdPoint) {
    Mat warp = null;
    Mat rectMat = null;
    try {
      warp = warpAffine(image, imagePoint, stdPoint);
      double imgWidth = warp.size().width;
      double imgHeight = warp.size().height;
      if (stdWidth <= imgWidth && stdHeight <= imgHeight) {
        Mat crop = new Mat(warp, new Rect(0, 0, stdWidth, stdHeight));
        return crop;
      }
      //计算需要裁剪的宽和高
      int h, w;
      if ((1.0 * imgWidth / imgHeight) >= (1.0 * stdWidth / stdHeight)) {
        h = (int) Math.floor(1.0 * imgHeight);
        w = (int) Math.floor(1.0 * stdWidth * imgHeight / stdHeight);

      } else {
        w = (int) Math.floor(1.0 * imgWidth);
        h = (int) Math.floor(1.0 * stdHeight * imgWidth / stdWidth);
      }
      //需要裁剪图片
      rectMat = new Mat(warp, new Rect(0, 0, w, h));
      Mat crop = new Mat();
      Imgproc.resize(rectMat, crop, new Size(stdWidth, stdHeight), 0, 0, Imgproc.INTER_NEAREST);
      return crop;
    } finally {
      if (null != rectMat) {
        rectMat.release();
      }
      if (null != warp) {
        warp.release();
      }
    }
  }


  /**
   * 图像仿射变换
   *
   * @param image    图像数据
   * @param imgPoint 图像中的关键点
   * @param stdPoint 定义的标准关键点
   * @return 图像的仿射结果图
   */
  public static Mat warpAffine(Mat image, double[][] imgPoint, double[][] stdPoint) {
    Mat matM = null;
    Mat matMTemp = null;
    try {
      //转换为矩阵
      RealMatrix imgPointMatrix = MathUtil.createMatrix(imgPoint);
      RealMatrix stdPointMatrix = MathUtil.createMatrix(stdPoint);
      //判断数据的行列是否一致
      int row = imgPointMatrix.getRowDimension();
      int col = imgPointMatrix.getColumnDimension();
      if (row <= 0 || col <= 0 || row != stdPointMatrix.getRowDimension()
          || col != stdPointMatrix.getColumnDimension()) {
        throw new RuntimeException("row or col is not equal");
      }
      //求列的均值
      RealVector imgPointMeanVector = MathUtil.mean(imgPointMatrix, 0);
      RealVector stdPointMeanVector = MathUtil.mean(stdPointMatrix, 0);
      //对关键点进行减去均值
      RealMatrix imgPointMatrix1 = imgPointMatrix.subtract(
          MathUtil.createMatrix(row, imgPointMeanVector.toArray()));
      RealMatrix stdPointMatrix1 = stdPointMatrix.subtract(
          MathUtil.createMatrix(row, stdPointMeanVector.toArray()));
      //计算关键点的标准差
      double imgPointStd = MathUtil.std(imgPointMatrix1);
      double stdPointStd = MathUtil.std(stdPointMatrix1);
      //对关键点除以标准差
      RealMatrix imgPointMatrix2 = MathUtil.scalarDivision(imgPointMatrix1, imgPointStd);
      RealMatrix stdPointMatrix2 = MathUtil.scalarDivision(stdPointMatrix1, stdPointStd);
      //获取矩阵的分量
      RealMatrix pointsT = imgPointMatrix2.transpose().multiply(stdPointMatrix2);
      SingularValueDecomposition svdH = new SingularValueDecomposition(pointsT);
      RealMatrix U = svdH.getU();
      RealMatrix S = svdH.getS();
      RealMatrix Vt = svdH.getVT();
      //计算仿射矩阵
      RealMatrix R = U.multiply(Vt).transpose();
      RealMatrix R1 = R.scalarMultiply(stdPointStd / imgPointStd);
      RealMatrix v21 = MathUtil.createMatrix(1, stdPointMeanVector.toArray()).transpose();
      RealMatrix v22 = R.multiply(
          MathUtil.createMatrix(1, imgPointMeanVector.toArray()).transpose());
      RealMatrix v23 = v22.scalarMultiply(stdPointStd / imgPointStd);
      RealMatrix R2 = v21.subtract(v23);
      RealMatrix M = MathUtil.hstack(R1, R2);
      //变化仿射矩阵为Mat
      matMTemp = new MatOfDouble(MathUtil.flatMatrix(M, 1).toArray());
      matM = new Mat(2, 3, CvType.CV_32FC3);
      matMTemp.reshape(1, 2).copyTo(matM);
      //使用open cv做仿射变换
      Mat dst = new Mat();
      Imgproc.warpAffine(image, dst, matM, image.size());
      return dst;
    } finally {
      if (null != matM) {
        matM.release();
      }
      if (null != matMTemp) {
        matMTemp.release();
      }
    }
  }

}
