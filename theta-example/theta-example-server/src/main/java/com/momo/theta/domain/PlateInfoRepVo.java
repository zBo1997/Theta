package com.momo.theta.domain;

import java.io.Serializable;

public class PlateInfoRepVo implements Serializable {
    /**车牌置信分数**/
    private Float score;

    /**车牌位置信息**/
    private PlateLocation location;

    /**车牌识别信息**/
    private RecognitionInfo recognition;

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public PlateLocation getLocation() {
        return location;
    }

    public void setLocation(PlateLocation location) {
        this.location = location;
    }

    public RecognitionInfo getRecognition() {
        return recognition;
    }

    public void setRecognition(RecognitionInfo recognition) {
        this.recognition = recognition;
    }
}
