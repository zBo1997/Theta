package com.momo.theta.domain;

import java.io.Serializable;

public class RecognitionInfo implements Serializable {
    /**车牌布局，单排还是双排**/
    private PlateLayout layout;
    /**车牌文本信息**/
    private String plateNo;
    /**车牌的颜色信息**/
    private PlateColor plateColor;

    public PlateLayout getLayout() {
        return layout;
    }

    public void setLayout(PlateLayout layout) {
        this.layout = layout;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public PlateColor getPlateColor() {
        return plateColor;
    }

    public void setPlateColor(PlateColor plateColor) {
        this.plateColor = plateColor;
    }
}
