package com.momo.theta.utils;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtSession;
import com.momo.theta.domain.BorderMat;
import com.momo.theta.domain.ImageMat;
import org.opencv.core.Mat;

public class ReleaseUtil {

  public static void release(Mat... mats) {
    for (Mat mat : mats) {
      if (null != mat) {
        try {
          mat.release();
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          mat = null;
        }
      }
    }
  }


  public static void release(ImageMat... imageMats) {
    for (ImageMat imageMat : imageMats) {
      if (null != imageMat) {
        try {
          imageMat.release();
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          imageMat = null;
        }
      }
    }
  }

  public static void release(BorderMat... borderMats) {
    for (BorderMat borderMat : borderMats) {
      if (null != borderMat) {
        try {
          borderMat.release();
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          borderMat = null;
        }
      }
    }
  }

  public static void release(OnnxTensor... tensors) {
    if (null == tensors || tensors.length == 0) {
      return;
    }
    try {
      for (OnnxTensor tensor : tensors) {
        try {
          if (null != tensor) {
            tensor.close();
          }
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          tensor = null;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      tensors = null;
    }
  }

  public static void release(OrtSession.Result... results) {
    if (null == results || results.length == 0) {
      return;
    }
    try {
      for (OrtSession.Result result : results) {
        try {
          if (null != result) {
            result.close();
          }
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          result = null;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      results = null;
    }
  }

}
