package com.momo.theta.service;

import com.momo.theta.entity.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @ClassName: IAsynExportExcelService
 * @Description:
 * @Author: WM
 * @Date: 2021-08-06 20:05
 **/
public interface IAsynExportExcelService {

    /**
     * 分批次异步导出数据
     *
     * @param countDownLatch
     */
    void executeAsyncTask(List<User> data, String filePath, CountDownLatch countDownLatch,Integer no);
}