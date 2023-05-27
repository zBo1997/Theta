package com.momo.theta.base;

import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtLoggingLevel;
import ai.onnxruntime.OrtSession;
import ai.onnxruntime.OrtSession.SessionOptions;

public abstract class BaseOnnxInfer {

  private OrtEnvironment env;
  private String[] inputNames;
  private OrtSession[] sessions;

  /**
   * 构造函数
   *
   * @param modelPath
   * @param threads
   */
  public BaseOnnxInfer(String modelPath, int threads, OrtEnvironment ortEnvironment,
      SessionOptions session) {
    try {
      this.env = ortEnvironment;
      OrtSession.SessionOptions opts = session;
      opts.setInterOpNumThreads(threads);
      opts.setSessionLogLevel(OrtLoggingLevel.ORT_LOGGING_LEVEL_ERROR);
      opts.setOptimizationLevel(OrtSession.SessionOptions.OptLevel.BASIC_OPT);
      this.sessions = new OrtSession[]{env.createSession(modelPath, opts)};
      this.inputNames = new String[]{this.sessions[0].getInputNames().iterator().next()};
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 构造函数
   *
   * @param modelPaths
   * @param threads
   */
  public BaseOnnxInfer(String[] modelPaths, int threads) {
    try {
      OrtEnvironment env = OrtEnvironment.getEnvironment();
      OrtSession.SessionOptions opts = new OrtSession.SessionOptions();
      opts.setInterOpNumThreads(threads);
      opts.setSessionLogLevel(OrtLoggingLevel.ORT_LOGGING_LEVEL_ERROR);
      opts.setOptimizationLevel(OrtSession.SessionOptions.OptLevel.BASIC_OPT);
      this.inputNames = new String[modelPaths.length];
      this.sessions = new OrtSession[modelPaths.length];
      for (int i = 0; i < modelPaths.length; i++) {
        OrtSession session = env.createSession(modelPaths[i], opts);
        this.sessions[i] = session;
        this.inputNames[i] = session.getInputNames().iterator().next();
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 获取环境信息
   *
   * @return
   */
  public OrtEnvironment getEnv() {
    return env;
  }

  /**
   * 获取输入端的名称
   *
   * @return
   */
  public String getInputName() {
    return inputNames[0];
  }

  /**
   * 获取session
   *
   * @return
   */
  public OrtSession getSession() {
    return sessions[0];
  }

  /**
   * 获取输入端的名称
   *
   * @return
   */
  public String[] getInputNames() {
    return inputNames;
  }

  /**
   * 获取session
   *
   * @return
   */
  public OrtSession[] getSessions() {
    return sessions;
  }

  /**
   * 关闭服务
   */
  public void close() {
    try {
      if (sessions != null) {
        for (OrtSession session : sessions) {
          session.close();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
