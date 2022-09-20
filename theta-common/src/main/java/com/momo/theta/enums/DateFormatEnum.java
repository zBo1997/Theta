package com.momo.theta.enums;

/**
 * 时间常量类
 */
public enum  DateFormatEnum {

    DATE_SHORT_DAY_ONLY("MMdd"),
    DATE_POINT_TIME_ONLY("HH:mm"),
    DATE_SHORT_TIME_ONLY("HHmmss"),
    DATE_SHORT_MONTH_ONLY("yyyyMM"),
    DATE_SHORT_DATE_ONLY("yyyyMMdd"),
    DATE_FORMAT_TIME_ONLY("HH:mm:ss"),
    DATE_FORMAT_MONTH_ONLY("yyyy-MM"),
    DATE_POINT_DATE_ONLY("yyyy.MM.dd"),
    DATE_FORMAT_DATE_ONLY("yyyy-MM-dd"),
    YEAR_MONTH_DAY_ONLY("yyyy年MM月dd日"),
    YEAR_MONTH_DAY_ONLY_ONE_NUM("yyyy年M月d日"),
    DATE_SHORT_DATETIME("yyyyMMddHHmmss"),
    DATE_FORMAT_DATETIME("yyyy-MM-dd HH:mm:ss"),
    ;

    private final String code;

    DateFormatEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
