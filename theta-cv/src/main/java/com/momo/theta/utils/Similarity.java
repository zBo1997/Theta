package com.momo.theta.utils;

import org.apache.commons.math3.linear.RealMatrix;

public class Similarity {

  /**
   * 向量余弦相似度
   *
   * @param leftVector
   * @param rightVector
   * @return
   */
  public static float cosineSimilarity(float[] leftVector, float[] rightVector) {
    double dotProduct = 0;
    for (int i = 0; i < leftVector.length; i++) {
      dotProduct += leftVector[i] * rightVector[i];
    }
    double d1 = 0.0d;
    for (float value : leftVector) {
      d1 += Math.pow(value, 2);
    }
    double d2 = 0.0d;
    for (float value : rightVector) {
      d2 += Math.pow(value, 2);
    }
    double cosineSimilarity;
    if (d1 <= 0.0 || d2 <= 0.0) {
      cosineSimilarity = 0.0;
    } else {
      cosineSimilarity = (dotProduct / (Math.sqrt(d1) * Math.sqrt(d2)));
    }
    return (float) cosineSimilarity;
  }


  /**
   * 两个向量可以为任意维度，但必须保持维度相同，表示n维度中的两点 欧式距离
   *
   * @param vector1
   * @param vector2
   * @return 两点间距离
   */
  public static float euclideanDistance(float[] vector1, float[] vector2) {
    double distance = 0;
    if (vector1.length == vector2.length) {
      for (int i = 0; i < vector1.length; i++) {
        double temp = Math.pow((vector1[i] - vector2[i]), 2);
        distance += temp;
      }
      distance = Math.sqrt(distance);
    } else {
      throw new RuntimeException("vector length not equal");
    }
    return (float) distance;
  }

  /**
   * 向量余弦相似度,加入了norm变换
   *
   * @param leftVector
   * @param rightVector
   * @return
   */
  public static float cosineSimilarityNorm(float[] leftVector, float[] rightVector) {
    RealMatrix rm1 = MathUtil.createMatrix(1, ArrayUtil.floatToDouble(leftVector));
    RealMatrix rm2 = MathUtil.createMatrix(1, ArrayUtil.floatToDouble(rightVector));
    RealMatrix num = rm1.multiply(rm2.transpose());
    double deco = ArrayUtil.matrixNorm(rm1.getData()) * ArrayUtil.matrixNorm(rm2.getData());
    double cos = num.getEntry(0, 0) / deco;
    double sim = cos;
    if (cos >= 0.5) {
      sim = cos + 2 * (cos - 0.5) * (1 - cos);
    } else if (cos >= 0) {
      sim = cos - 2 * (cos - 0.5) * (0 - cos);
    }
    return Double.valueOf(sim).floatValue();
  }

  /**
   * 对cos的原始值进行进行增强
   *
   * @param cos
   * @return
   */
  public static float cosEnhance(float cos) {
    double sim = cos;
    if (cos >= 0.5) {
      sim = cos + 2 * (cos - 0.5) * (1 - cos);
    } else if (cos >= 0) {
      sim = cos - 2 * (cos - 0.5) * (0 - cos);
    }
    return Double.valueOf(sim).floatValue();
  }


}
