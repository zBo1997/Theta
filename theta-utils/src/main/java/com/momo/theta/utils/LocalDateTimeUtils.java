package com.momo.theta.utils;

import com.momo.theta.enums.DateFormatEnum;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalDateTimeUtils {

  private static Logger log = LoggerFactory.getLogger(LocalDateTimeUtils.class);

  private LocalDateTimeUtils() {

  }

  /**
   * 格式化时间成HH:mm
   *
   * @param date 格式必须是 HH:mm:ss
   * @return
   */
  public static String formatHHmm(String date) {
    String format = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    String concat = format.concat(" " + date);
    LocalDateTime localDateTime = LocalDateTime.parse(concat,
        DateTimeFormatter.ofPattern(DateFormatEnum.DATE_FORMAT_DATETIME.getCode()));
    return formatDateTime(localDateTime, DateFormatEnum.DATE_POINT_TIME_ONLY);
  }

  /**
   * 格式化时间
   *
   * @param localDateTime
   * @return
   */
  public static String formatDateTime(LocalDateTime localDateTime, DateFormatEnum formatEnum) {
    String dateTimeStr = StringUtils.EMPTY;
    if (Objects.isNull(localDateTime) || Objects.isNull(formatEnum)) {
      return dateTimeStr;
    }
    return localDateTime.format(DateTimeFormatter.ofPattern(formatEnum.getCode()));
  }

  /**
   * 格式化时间
   *
   * @param localDate
   * @return
   */
  public static String formatDate(LocalDate localDate, DateFormatEnum formatEnum) {
    String dateTimeStr = StringUtils.EMPTY;
    if (Objects.isNull(localDate) || Objects.isNull(formatEnum)) {
      return dateTimeStr;
    }
    return localDate.format(DateTimeFormatter.ofPattern(formatEnum.getCode()));
  }

  /**
   * 拼接当天最早时间
   *
   * @param day 必须是yyyy-MM-dd
   * @return
   */
  public static String currentDayBeginDate(String day) {
    DateTimeFormatter df = DateTimeFormatter.ofPattern(
        DateFormatEnum.DATE_FORMAT_DATE_ONLY.getCode());
    LocalDate localDate = LocalDate.parse(day, df);
    return formatDateTime(localDate.atTime(0, 0, 0), DateFormatEnum.DATE_FORMAT_DATETIME);
  }

  /**
   * 拼接当天最晚时间
   *
   * @param day
   * @return
   */
  public static String currentDayEndDate(String day) {
    DateTimeFormatter df = DateTimeFormatter.ofPattern(
        DateFormatEnum.DATE_FORMAT_DATE_ONLY.getCode());
    LocalDate localDate = LocalDate.parse(day, df);
    return formatDateTime(localDate.atTime(23, 59, 59), DateFormatEnum.DATE_FORMAT_DATETIME);
  }

  /**
   * 检验当前时间是否在周期范围内
   *
   * @param beginDate   开始日期
   * @param endDate     结束日期
   * @param beginPeriod 开始时段
   * @param endPeriod   结束时段
   * @param cycle       周期（0每天，1-7周天）
   * @return true?符合:不符合
   */
  public static boolean checkCycle(LocalDate beginDate, LocalDate endDate, String beginPeriod,
      String endPeriod,
      String cycle) {
    try {
      LocalDate now = LocalDate.now();
      // 1.检验日期范围内
      if (now.isBefore(beginDate) || now.isAfter(endDate)) {
        return Boolean.FALSE;
      }
      return checkCycleExceptLocalDate(beginPeriod, endPeriod, cycle, now);
    } catch (Exception e) {
      log.error("检验当前时间是否在周期范围内异常", e);
      return Boolean.FALSE;
    }
  }

  /**
   * 检验当前时间是否在周期范围内
   *
   * @param beginDate   开始日期
   * @param endDate     结束日期
   * @param beginPeriod 开始时段
   * @param endPeriod   结束时段
   * @param cycle       周期（0每天，1-7周天）
   * @return true?符合:不符合
   */
  public static boolean checkCycle(LocalDateTime beginDate, LocalDateTime endDate,
      String beginPeriod,
      String endPeriod, String cycle) {
    try {
      LocalDateTime now = LocalDateTime.now();
      // 1. 校验日期
      if (Objects.isNull(beginDate) || Objects.isNull(endDate)) {
        return Boolean.FALSE;
      }
      if (now.isBefore(beginDate) || now.isAfter(endDate)) {
        return Boolean.FALSE;
      }
      return checkCycleExceptLocalDate(beginPeriod, endPeriod, cycle, LocalDate.now());
    } catch (Exception e) {
      log.error("检验当前时间是否在周期范围内异常", e);
      return Boolean.FALSE;
    }
  }

  /**
   * 检查当前时间除LocalDate/LocalDateTime
   *
   * @param beginPeriod
   * @param endPeriod
   * @param cycle
   * @param now
   * @return
   */
  private static boolean checkCycleExceptLocalDate(String beginPeriod, String endPeriod,
      String cycle,
      LocalDate now) {
    // 2.检验时间段范围内
    if (StringUtils.isBlank(beginPeriod) || StringUtils.isBlank(endPeriod)) {
      return Boolean.FALSE;
    }
    LocalTime nowTime = LocalTime.now();
    String[] beginPeriodTime = beginPeriod.split(":");
    LocalTime beginTime = LocalTime.of(Integer.parseInt(beginPeriodTime[0]),
        Integer.parseInt(beginPeriodTime[1]),
        Integer.parseInt(beginPeriodTime[2]));
    String[] endPeriodTime = endPeriod.split(":");
    LocalTime endTime = LocalTime.of(Integer.parseInt(endPeriodTime[0]),
        Integer.parseInt(endPeriodTime[1]),
        Integer.parseInt(endPeriodTime[2]));
    if (nowTime.isBefore(beginTime) || nowTime.isAfter(endTime)) {
      return Boolean.FALSE;
    }
    // 3.检验周期范围内
    if (Boolean.FALSE.equals(cycle.contains("0"))) {
      // 获取当前周几
      int week = now.getDayOfWeek().get(ChronoField.DAY_OF_WEEK);
      if (Boolean.FALSE.equals(cycle.contains(String.valueOf(week)))) {
        return Boolean.FALSE;
      }
    }
    return Boolean.TRUE;
  }

}
