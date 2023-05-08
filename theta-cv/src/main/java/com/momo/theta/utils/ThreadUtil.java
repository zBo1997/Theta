package com.momo.theta.utils;

public class ThreadUtil {

  public static void run(Runnable runnable) {
    new Thread(runnable).start();
  }

}
