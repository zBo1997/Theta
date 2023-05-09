package com.momo.theta.utils;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * SoftMaxUtil
 */
public class SoftMaxUtil {

  /**
   * softMax
   *
   * @param tensor
   * @return
   */
  public static double[] softMax(double[] tensor) {
    if (Arrays.stream(tensor).max().isPresent()) {
      double maxValue = Arrays.stream(tensor).max().getAsDouble();
      double[] value = Arrays.stream(tensor).map(y -> Math.exp(y - maxValue)).toArray();
      double total = Arrays.stream(value).sum();
      return Arrays.stream(value).map(p -> p / total).toArray();
    } else {
      throw new NoSuchElementException("No value present");
    }
  }

  /**
   * softMax
   *
   * @param tensor
   * @return
   */
  public double[][] softMax(double[][] tensor) {
    double[][] result = new double[tensor.length][];
    for (int i = 0; i < result.length; i++) {
      result[i] = softMax(tensor[i]);
    }
    return result;
  }

}
