package com.momo.theta.utils;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

public class MathUtil {

  /**
   * 创建向量
   *
   * @param array 数组
   * @return 向量
   */
  public static RealVector createVector(double[] array) {
    return new ArrayRealVector(array);
  }

  /**
   * 创建向量
   *
   * @param array 数组
   * @return 向量
   */
  public static RealVector createVector(Double[] array) {
    return new ArrayRealVector(array);
  }


  /**
   * 创建矩阵
   *
   * @param array 矩阵数组
   * @return 矩阵
   */
  public static RealMatrix createMatrix(double[][] array) {
    return new Array2DRowRealMatrix(array);
  }

  /**
   * 创建矩阵
   *
   * @param array 矩阵数组
   * @return 矩阵
   */
  public static RealMatrix createMatrix(Double[][] array) {
    double[][] data = new double[array.length][];
    for (int i = 0; i < array.length; i++) {
      double[] item = new double[array[i].length];
      for (int j = 0; j < array[i].length; j++) {
        item[j] = array[i][j];
      }
      data[i] = item;
    }
    return new Array2DRowRealMatrix(data);
  }

  /**
   * 创建矩阵
   *
   * @param rows  重复的行数
   * @param array 矩阵数组
   * @return 矩阵
   */
  public static RealMatrix createMatrix(int rows, double[] array) {
    double[][] data = new double[rows][array.length];
    for (int i = 0; i < rows; i++) {
      data[i] = array;
    }
    return new Array2DRowRealMatrix(data);
  }

  /**
   * 将矩阵的每个值都加上value值
   *
   * @param matrix 矩阵
   * @param value  加值
   * @return 矩阵
   */
  public static RealMatrix scalarAdd(RealMatrix matrix, double value) {
    return matrix.scalarAdd(value);
  }

  /**
   * 将矩阵的每个值都减去value值
   *
   * @param matrix 矩阵
   * @param value  减值
   * @return 矩阵
   */
  public static RealMatrix scalarSub(RealMatrix matrix, double value) {
    return matrix.scalarAdd(-value);
  }

  /**
   * 将矩阵的每个值都乘以value值
   *
   * @param matrix 矩阵
   * @param value  乘值
   * @return 矩阵
   */
  public static RealMatrix scalarMultiply(RealMatrix matrix, double value) {
    return matrix.scalarMultiply(value);
  }

  /**
   * 将矩阵的每个值都除以value值
   *
   * @param matrix 矩阵
   * @param value  除值
   * @return 矩阵
   */
  public static RealMatrix scalarDivision(RealMatrix matrix, double value) {
    return matrix.scalarMultiply(1.0 / value);
  }

  /**
   * 求矩阵的均值，分坐标轴：0：Y轴， 1：X轴
   *
   * @param matrix 数据矩阵
   * @param axis   0：Y轴， 1：X轴
   * @return 均值
   */
  public static RealVector mean(RealMatrix matrix, int axis) {
    if (axis == 0) {
      double[] means = new double[matrix.getColumnDimension()];
      for (int i = 0; i < matrix.getColumnDimension(); i++) {
        means[i] = new Mean().evaluate(matrix.getColumn(i));
      }
      return new ArrayRealVector(means);
    } else {
      double[] means = new double[matrix.getRowDimension()];
      for (int i = 0; i < matrix.getRowDimension(); i++) {
        means[i] = new Mean().evaluate(matrix.getRow(i));
      }
      return new ArrayRealVector(means);
    }
  }

  /**
   * 计算矩阵的整体标准差
   *
   * @param matrix 数据矩阵
   * @return 整体标准差
   */
  public static double std(RealMatrix matrix) {
    double[] data = new double[matrix.getColumnDimension() * matrix.getRowDimension()];
    for (int i = 0; i < matrix.getRowDimension(); i++) {
      for (int j = 0; j < matrix.getColumnDimension(); j++) {
        data[i * matrix.getColumnDimension() + j] = matrix.getEntry(i, j);
      }
    }
    return new StandardDeviation(false).evaluate(data);
  }

  /**
   * 矩阵列拼接
   *
   * @param matrix1 数据矩阵1
   * @param matrix2 数据矩阵2
   * @return 数据矩阵
   */
  public static RealMatrix hstack(RealMatrix matrix1, RealMatrix matrix2) {
    int row = matrix1.getRowDimension();
    int col = matrix1.getColumnDimension() + matrix2.getColumnDimension();
    double[][] data = new double[row][col];
    for (int i = 0; i < matrix1.getRowDimension(); i++) {
      for (int j = 0; j < matrix1.getColumnDimension(); j++) {
        data[i][j] = matrix1.getEntry(i, j);
      }
      for (int j = 0; j < matrix2.getColumnDimension(); j++) {
        data[i][matrix1.getColumnDimension() + j] = matrix2.getEntry(i, j);
      }
    }
    return new Array2DRowRealMatrix(data);
  }

  /**
   * 矩阵行拼接
   *
   * @param matrix1 数据矩阵1
   * @param matrix2 数据矩阵2
   * @return 数据矩阵
   */
  public static RealMatrix vstack(RealMatrix matrix1, RealMatrix matrix2) {
    int row = matrix1.getRowDimension() + matrix2.getRowDimension();
    int col = matrix1.getColumnDimension();
    double[][] data = new double[row][col];
    for (int i = 0; i < matrix1.getRowDimension(); i++) {
      for (int j = 0; j < matrix1.getColumnDimension(); j++) {
        data[i][j] = matrix1.getEntry(i, j);
      }
    }
    for (int i = 0; i < matrix2.getRowDimension(); i++) {
      for (int j = 0; j < matrix2.getColumnDimension(); j++) {
        data[i + matrix1.getRowDimension()][j] = matrix2.getEntry(i, j);
      }
    }
    return new Array2DRowRealMatrix(data);
  }

  /**
   * 将矩阵拉平
   *
   * @param matrix 矩阵
   * @param axis   0：Y轴， 1：X轴
   * @return
   */
  public static RealVector flatMatrix(RealMatrix matrix, int axis) {
    RealVector vector = new ArrayRealVector();
    if (0 == axis) {
      for (int i = 0; i < matrix.getColumnDimension(); i++) {
        vector = vector.append(matrix.getColumnVector(i));
      }
    } else {
      for (int i = 0; i < matrix.getRowDimension(); i++) {
        vector = vector.append(matrix.getRowVector(i));
      }
    }
    return vector;
  }

  /**
   * 向量点积
   *
   * @param vector1 向量1
   * @param vector2 向量2
   * @return 点积
   */
  public static double dotProduct(RealVector vector1, RealVector vector2) {
    return vector1.dotProduct(vector2);
  }

  /**
   * 矩阵点积
   *
   * @param matrix1 矩阵1
   * @param matrix2 矩阵2
   * @return 点积矩阵
   */
  public static RealMatrix dotProduct(RealMatrix matrix1, RealMatrix matrix2) {
    double[][] data = new double[matrix1.getRowDimension()][matrix1.getColumnDimension()];
    for (int row = 0; row < matrix1.getRowDimension(); row++) {
      for (int col = 0; col < matrix1.getColumnDimension(); col++) {
        data[row][col] = matrix1.getRowVector(row).dotProduct(matrix2.getColumnVector(col));
      }
    }
    return createMatrix(data);
  }

  /**
   * 矩阵相似变换
   *
   * @param matrix
   * @param scale
   * @param rotation
   * @param translation
   * @return
   */
  public static RealMatrix similarityTransform(Double[][] matrix, Double scale, Double rotation,
      Double[] translation) {
    if (matrix == null && translation == null) {
      return similarityTransform((RealMatrix) null, scale, rotation, null);
    } else if (matrix == null) {
      return similarityTransform(null, scale, rotation, createVector(translation));
    } else if (translation == null) {
      return similarityTransform(createMatrix(matrix), scale, rotation, null);
    } else {
      return similarityTransform(createMatrix(matrix), scale, rotation, createVector(translation));
    }
  }

  /**
   * 矩阵相似变换
   *
   * @param matrix
   * @param scale
   * @param rotation
   * @param translation
   * @return
   */
  public static RealMatrix similarityTransform(RealMatrix matrix, Double scale, Double rotation,
      RealVector translation) {
    boolean hasParams = (scale != null || rotation != null || translation != null);
    if (hasParams && matrix != null) {
      throw new RuntimeException(
          "You cannot specify the transformation matrix and the implicit parameters at the same time.");
    } else if (matrix != null) {
      if (matrix.getColumnDimension() != 3 && matrix.getRowDimension() != 3) {
        throw new RuntimeException("Invalid shape of transformation matrix.");
      } else {
        return matrix;
      }
    } else if (hasParams) {
      scale = scale == null ? 1 : scale;
      rotation = rotation == null ? 0 : rotation;
      translation = translation == null ? createVector(new double[]{0, 0}) : translation;
      return createMatrix(new double[][]{
          {Math.cos(rotation) * scale, -Math.sin(rotation) * scale, translation.getEntry(0)},
          {Math.sin(rotation) * scale, Math.cos(rotation) * scale, translation.getEntry(1)},
          {0, 0, 1}
      });
    } else {
      return createMatrix(new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}});
    }
  }

}
