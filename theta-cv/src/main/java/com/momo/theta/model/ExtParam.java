package com.momo.theta.model;

import java.io.Serializable;

public class ExtParam implements Serializable {

  private float scoreTh;

  private float iouTh;

  private int topK = 5;

  private ExtParam() {
  }

  public static ExtParam build() {
    return new ExtParam();
  }

  public float getScoreTh() {
    return scoreTh;
  }

  public ExtParam setScoreTh(float scoreTh) {
    this.scoreTh = scoreTh;
    return this;
  }

  public float getIouTh() {
    return iouTh;
  }

  public ExtParam setIouTh(float iouTh) {
    this.iouTh = iouTh;
    return this;
  }

  public int getTopK() {
    return topK;
  }

  public ExtParam setTopK(int topK) {
    this.topK = topK;
    return this;
  }
}
