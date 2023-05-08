package com.momo.theta.utils;

import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.export.ExcelExportService;
import cn.afterturn.easypoi.exception.excel.ExcelExportException;
import cn.afterturn.easypoi.exception.excel.enums.ExcelExportEnum;
import cn.afterturn.easypoi.util.PoiPublicUtil;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @ClassName: MyExcelExportService
 * @Description:
 * @Author: WM
 * @Date: 2021-07-30 16:54
 **/
@Slf4j
public class MyExcelExportService {

  private static double EXCEL_WIDTH = 25.0D;

  public void createSheet(Workbook workbook, ExportParams entity, Class<?> pojoClass,
      Collection<?> dataSet) {
    if (log.isDebugEnabled()) {
      log.debug("Excel export start ,class is {}", pojoClass);
      log.debug("Excel version is {}", entity.getType().equals(ExcelType.HSSF) ? "03" : "07");
    }

    if (workbook != null && entity != null && pojoClass != null && dataSet != null) {
      try {
        List<ExcelExportEntity> excelParams = new ArrayList();
        Field[] fileds = PoiPublicUtil.getClassFields(pojoClass);
        ExcelTarget etarget = pojoClass.getAnnotation(ExcelTarget.class);
        String targetId = etarget == null ? null : etarget.value();
        ExcelExportService excelExportService = new ExcelExportService();
        excelExportService.getAllExcelField(entity.getExclusions(), targetId, fileds, excelParams,
            pojoClass, null, (ExcelEntity) null);
        excelParams.stream().forEach(x -> x.setWidth(EXCEL_WIDTH));
        excelExportService.createSheetForMap(workbook, entity, excelParams, dataSet);
      } catch (Exception var9) {
        log.error(var9.getMessage(), var9);
        throw new ExcelExportException(ExcelExportEnum.EXPORT_ERROR, var9.getCause());
      }
    } else {
      throw new ExcelExportException(ExcelExportEnum.PARAMETER_ERROR);
    }
  }
}