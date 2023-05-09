package com.momo.theta.utils;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.entity.params.ExcelForEachParams;
import cn.afterturn.easypoi.excel.export.styler.IExcelExportStyler;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @ClassName: ExcelStyleUtil
 * @Description: Excel样式设置
 * @Author: WM
 * @Date: 2021-07-24 18:23
 **/
public class ExcelStyleUtil implements IExcelExportStyler {

  private static final short STRING_FORMAT = (short) BuiltinFormats.getBuiltinFormat("TEXT");
  private static final short FONT_SIZE_TEN = 10;
  private static final short FONT_SIZE_ELEVEN = 11;
  private static final short FONT_SIZE_TWELVE = 12;

  // 标题头样式
  private CellStyle headerStyle;
  // 数据列标题样式
  private CellStyle titleStyle;
  // 数据行样式
  private CellStyle styles;

  public ExcelStyleUtil(Workbook workbook) {
    this.init(workbook);
  }

  /**
   * 初始化样式
   *
   * @param workbook
   */
  private void init(Workbook workbook) {
    this.headerStyle = initHeaderStyle(workbook);
    this.titleStyle = initTitleStyle(workbook);
    this.styles = initStyles(workbook);
  }

  /**
   * 标题头样式
   *
   * @param color
   * @return
   */
  @Override
  public CellStyle getHeaderStyle(short color) {
    return headerStyle;
  }

  /**
   * 数据列标题样式
   *
   * @param color
   * @return
   */
  @Override
  public CellStyle getTitleStyle(short color) {
    return titleStyle;
  }

  /**
   * 数据行样式
   *
   * @param parity            表示奇偶行
   * @param excelExportEntity 数据内容
   * @return
   */
  @Override
  public CellStyle getStyles(boolean parity, ExcelExportEntity excelExportEntity) {
    return styles;
  }

  /**
   * 数据行样式
   *
   * @param cell              单元格
   * @param dataRow           数据行
   * @param excelExportEntity 数据内容
   * @param obj               对象
   * @param data              数据
   * @return
   */
  @Override
  public CellStyle getStyles(Cell cell, int dataRow, ExcelExportEntity excelExportEntity,
      Object obj, Object data) {
    return getStyles(true, excelExportEntity);
  }

  /**
   * 模板使用的样式设置
   *
   * @param b
   * @param excelForEachParams
   * @return
   */
  @Override
  public CellStyle getTemplateStyles(boolean b, ExcelForEachParams excelForEachParams) {
    return null;
  }

  /**
   * 初始化--标题头样式
   *
   * @param workbook
   * @return
   */
  private CellStyle initHeaderStyle(Workbook workbook) {
    CellStyle style = getBaseCellStyle(workbook);
    style.setFont(getFont(workbook, FONT_SIZE_TWELVE, true));
    return style;
  }

  /**
   * 初始化--数据列标题样式
   *
   * @param workbook
   * @return
   */
  private CellStyle initTitleStyle(Workbook workbook) {
    CellStyle style = getBaseCellStyle(workbook);
    style.setFont(getFont(workbook, FONT_SIZE_ELEVEN, false));
    // 背景色
    style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    return style;
  }

  /**
   * 初始化--数据行样式
   *
   * @param workbook
   * @return
   */
  private CellStyle initStyles(Workbook workbook) {
    CellStyle style = getBaseCellStyle(workbook);
    style.setFont(getFont(workbook, FONT_SIZE_TEN, false));
    style.setDataFormat(STRING_FORMAT);
    return style;
  }

  /**
   * 基础样式
   *
   * @return
   */
  private CellStyle getBaseCellStyle(Workbook workbook) {
    CellStyle style = workbook.createCellStyle();
    // 下边框
    style.setBorderBottom(BorderStyle.THIN);
    // 左边框
    style.setBorderLeft(BorderStyle.THIN);
    // 上边框
    style.setBorderTop(BorderStyle.THIN);
    // 右边框
    style.setBorderRight(BorderStyle.THIN);
    // 水平居中
    style.setAlignment(HorizontalAlignment.CENTER);
    // 上下居中
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    // 设置自动换行
    style.setWrapText(true);

    return style;
  }

  /**
   * 字体样式
   *
   * @param size   字体大小
   * @param isBold 是否加粗
   * @return
   */
  private Font getFont(Workbook workbook, short size, boolean isBold) {
    Font font = workbook.createFont();
    // 字体样式
    font.setFontName("宋体");
    // 是否加粗
    font.setBold(isBold);
    // 字体大小
    font.setFontHeightInPoints(size);
    return font;
  }

//    private void setSheet(Workbook workbook, int width) {
//        Sheet sheet = workbook.createSheet();
//        //设置单元格列宽度
//        sheet.autoSizeColumn(0);
//        sheet.setColumnWidth(0, 7000);
//    }
}