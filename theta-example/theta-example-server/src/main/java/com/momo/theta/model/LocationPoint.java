package com.momo.theta.model;

import java.io.Serializable;

public class LocationPoint implements Serializable {

    /**坐标X的值**/
    private int x;
    /**坐标Y的值**/
    private int y;

    public LocationPoint(){}

    public LocationPoint(float x, float y) {
        this.x = (int)x;
        this.y = (int)y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
