package com.momo.theta.service.impl;

import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.momo.theta.entity.User;
import com.momo.theta.service.IAsynExportExcelService;
import com.momo.theta.service.IUserService;
import com.momo.theta.utils.MyExcelExportUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @ClassName: IAsynExportExcelServiceImpl
 * @Description:
 * @Author: WM
 * @Date: 2021-08-06 20:06
 **/
@Service
@Slf4j
public class IAsynExportExcelServiceImpl implements IAsynExportExcelService {

    @Autowired
    private IUserService userService;

    @Override
    @Async("exportTaskExecutor")
    public void executeAsyncTask(List<User> data, String path, CountDownLatch cdl, Integer no) {
        long start = System.currentTimeMillis();
        log.info("线程：" + Thread.currentThread().getName() + " , 读取数据，耗时 ：" + (System.currentTimeMillis() - start));
        String filePath = path + no + ".xlsx";
        // 调用导出的文件方法
        Workbook workbook = MyExcelExportUtil.getWorkbook("计算机一班学生", "学生", User.class, data, ExcelType.XSSF);
        File file = new File(filePath);
        MyExcelExportUtil.exportExcel2(workbook, file);
        long end = System.currentTimeMillis();
        log.info("线程：" + Thread.currentThread().getName() + " , 导出excel" + no + ".xlsx成功 , 导出数据：" + data.size() + " ,耗时 ：" + (end - start) + "ms");
        // 执行完线程数减1
        cdl.countDown();
        log.info("剩余任务数  ===========================> " + cdl.getCount());
    }

}