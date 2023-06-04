package com.momo.theta.model;

public enum PlateColor {

    BLACK("黑色"),
    BLUE("蓝色"),
    GREEN("绿色"),
    WHITE("白色"),
    YELLOW("黄色"),
    UNKNOWN("未知");

    private String name;

    PlateColor(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public static PlateColor valueOfName(String name){
        for (PlateColor item : PlateColor.values()){
            if(item.name.equals(name)){
                return item;
            }
        }
        return UNKNOWN;
    }

}
