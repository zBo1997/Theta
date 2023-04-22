package com.momo.theta.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.momo.theta.entity.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * IAsynExportExcelService
 */
public interface IAsynExportExcelService {

    /**
     * 分批次异步导出数据
     *
     * @param countDownLatch
     */
    void executeAsyncTask(Page page, String filePath, CountDownLatch countDownLatch);
}