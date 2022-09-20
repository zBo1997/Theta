package com.momo.theta.constants;

import lombok.extern.slf4j.Slf4j;

/**
 * 延迟队列枚举
 */
@Slf4j
public enum DelayLevelEnum {
    L1(1,1,1000,"1s"),
    L2(2,5,5000,"5s"),
    L3(3,10,10000,"10s"),
    L4(4,30,30000,"30s"),
    L5(5,60,60000,"1m"),
    L6(6,120,120000,"2m"),
    L7(7,180,180000,"3m"),
    L8(8,240,240000,"4m"),
    L9(9,300,300000,"5m"),
    L10(10,360,360000,"6m"),
    L11(11,420,420000,"7m"),
    L12(12,480,480000,"8m"),
    L13(13,540,540000,"9m"),
    L14(14,600,600000,"10m"),
    L15(15,1200,1200000,"20m"),
    L16(16,1800,1800000,"30m"),
    L17(17,3600,3600000,"1h"),
    L18(18,7200,7200000,"2h"),
    ;

    private int level;
    private int second;
    private int millisecond;
    private String desc;

    DelayLevelEnum(int level, int second, int millisecond, String desc) {
        this.level = level;
        this.second = second;
        this.millisecond = millisecond;
        this.desc = desc;
    }

    public static long getLevelMills(int level){
        try{
            DelayLevelEnum levelEnum = DelayLevelEnum.valueOf("L"+level);
            return levelEnum.millisecond;
        }catch (Exception e){
            log.error("延迟枚举转换错误",e);
            return L2.millisecond;
        }
    }
}
