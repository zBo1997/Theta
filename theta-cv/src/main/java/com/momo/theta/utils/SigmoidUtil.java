package com.momo.theta.utils;

import java.util.function.DoubleUnaryOperator;


/**
 * SigmoidUtil
 */
public class SigmoidUtil {

  private static final DoubleUnaryOperator sigmoid = p -> 1 / (1 + Math.exp(-1 * p));

  /**
   * sigmoid
   *
   * @param tensor
   * @return
   */
  public static double[] sigmoid(double[] tensor) {
    double[] result = new double[tensor.length];
    for (int i = 0; i < result.length; i++) {
      result[i] = sigmoid.applyAsDouble(tensor[i]);
    }
    return result;
  }

  /**
   * sigmoid
   *
   * @param tensor
   * @return
   */
  public static double[][] sigmoid(double[][] tensor) {
    double[][] result = new double[tensor.length][];
    for (int i = 0; i < result.length; i++) {
      result[i] = sigmoid(tensor[i]);
    }
    return result;
  }

}
