package com.momo.theta.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.momo.theta.entity.User;
import com.momo.theta.excel.ZipUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @ClassName:AsynExcelExportUtil
 * @Description: 多线程批量导出excel工具类
 * @Author:Deamer
 * @Date:2021/8/8 23:00
 **/
@Slf4j
@Component
public class AsynExcelExportUtil {

  /**
   * 每批次处理的数据量
   */
  private static final int LIMIT = 5000;
  public static Queue<Map<String, Object>> queue;

  static {
    // 一个基于链接节点的无界线程安全的队列
    queue = new ConcurrentLinkedQueue<>();
  }

  /**
   *
   */
  private String filePath = "C:\\Users\\zhubo\\game\\";
  @Resource
  private IAsynExportExcelService asynExportExcelService;
  @Autowired
  private IUserService userService;

  /**
   * 多线程批量导出 excel
   *
   * @param response 用于浏览器下载
   * @throws InterruptedException
   */
  public void threadExcel(HttpServletResponse response) throws InterruptedException {
    long start = System.currentTimeMillis();
    // 数据的总数
    long dataTotalCount = userService.getCount();
    int listCount = (int) dataTotalCount;
    //if (dataTotalCount >= 5000){
    //    listCount = 5000;
    //}
    // 计算出多少页，即循环次数
    int count = listCount / LIMIT + (listCount % LIMIT > 0 ? 1 : 0);

    //异步转同步，等待所有线程都执行完毕返回 主线程才会结束
    try {
      CountDownLatch cdl = new CountDownLatch(count);
      for (int i = 1; i <= count; i++) {
        Page<User> page = Page.<User>of(i, LIMIT);
        asynExportExcelService.executeAsyncTask(page, filePath, cdl);
      }
      cdl.await();
      log.info("excel导出完成·······················");
      //压缩文件
      File zipFile = new File(filePath.substring(0, filePath.length() - 1) + ".zip");
      FileOutputStream fos1 = new FileOutputStream(zipFile);
      //压缩文件目录
      ZipUtils.toZip(filePath, fos1, true);
      //发送zip包
      ZipUtils.sendZip(response, zipFile);
    } catch (Exception e) {
      e.printStackTrace();
    }
    long end = System.currentTimeMillis();
    log.info("任务执行完毕共消耗：  " + (end - start) + "ms");
  }


}