package com.momo.theta.model;

import java.io.Serializable;

public class PlateInfo implements Comparable<PlateInfo>, Serializable {

  /**
   * 车牌分数
   **/
  public float score;
  /**
   * 车牌旋转角度
   **/
  public float angle;
  /**
   * 车牌框
   **/
  public PlateBox box;
  /**
   * 是否为单行车牌
   **/
  public boolean single;
  /**
   * 解析到的车牌信息
   **/
  public ParseInfo parseInfo;

  /**
   * 构造函数
   *
   * @param score 车牌分数
   * @param box   车牌框
   * @param angle 车牌旋转角度
   */
  private PlateInfo(float score, PlateBox box, float angle, boolean single) {
    this.score = score;
    this.angle = angle;
    this.box = box;
    this.single = single;
  }

  /**
   * 构造一个车牌信息
   *
   * @param score 车牌分数
   * @param box   车牌框
   */
  public static PlateInfo build(float score, PlateBox box) {
    return new PlateInfo(score, box, 0, true);
  }

  /**
   * 构造一个车牌信息
   *
   * @param score 车牌分数
   * @param box   车牌框
   */
  public static PlateInfo build(float score, PlateBox box, boolean single) {
    return new PlateInfo(score, box, 0, single);
  }

  /**
   * 构造一个车牌信息
   *
   * @param score 车牌分数
   * @param box   车牌框
   * @param angle 车牌旋转角度
   */
  public static PlateInfo build(float score, PlateBox box, float angle) {
    return new PlateInfo(score, box, angle, true);
  }

  /**
   * 对车牌框进行旋转对应的角度
   *
   * @return
   */
  public PlateBox rotatePlateBox() {
    return this.box.rotate(this.angle);
  }

  @Override
  public int compareTo(PlateInfo that) {
    return Float.compare(that.score, this.score);
  }

  /**
   * 关键点
   */
  public static class Point implements Serializable {

    /**
     * 坐标X的值
     **/
    public float x;
    /**
     * 坐标Y的值
     **/
    public float y;

    /**
     * 构造函数
     *
     * @param x 坐标X的值
     * @param y 坐标Y的值
     */
    private Point(float x, float y) {
      this.x = x;
      this.y = y;
    }

    /**
     * 构造一个点
     *
     * @param x 坐标X的值
     * @param y 坐标Y的值
     * @return
     */
    public static Point build(float x, float y) {
      return new Point(x, y);
    }

    /**
     * 对点进行中心旋转
     *
     * @param center 中心点
     * @param angle  旋转角度
     * @return 旋转后的角
     */
    public Point rotation(Point center, float angle) {
      double k = Math.toRadians(angle);
      float nx1 = (float) ((this.x - center.x) * Math.cos(k) + (this.y - center.y) * Math.sin(k)
          + center.x);
      float ny1 = (float) (-(this.x - center.x) * Math.sin(k) + (this.y - center.y) * Math.cos(k)
          + center.y);
      return new Point(nx1, ny1);
    }

    /**
     * 计算两点之间的距离
     *
     * @param that 点
     * @return 距离
     */
    public float distance(Point that) {
      return (float) Math.sqrt(Math.pow((this.x - that.x), 2) + Math.pow((this.y - that.y), 2));
    }
  }

  /**
   * 标准坐标系下的车牌框
   */
  public static class PlateBox implements Serializable {

    /**
     * 左上角坐标值
     **/
    public Point leftTop;
    /**
     * 右上角坐标
     **/
    public Point rightTop;
    /**
     * 右下角坐标
     **/
    public Point rightBottom;
    /**
     * 左下角坐标
     **/
    public Point leftBottom;

    /**
     * 构造函数
     *
     * @param leftTop     左上角坐标值
     * @param rightTop    右上角坐标
     * @param rightBottom 右下角坐标
     * @param leftBottom  左下角坐标
     */
    private PlateBox(Point leftTop, Point rightTop, Point rightBottom, Point leftBottom) {
      this.leftTop = leftTop;
      this.rightTop = rightTop;
      this.rightBottom = rightBottom;
      this.leftBottom = leftBottom;
    }

    /**
     * 构造函数
     *
     * @param x1 左上角坐标X的值
     * @param y1 左上角坐标Y的值
     * @param x2 右下角坐标X的值
     * @param y2 右下角坐标Y的值
     */
    private PlateBox(float x1, float y1, float x2, float y2) {
      this.leftTop = Point.build(x1, y1);
      this.rightTop = Point.build(x2, y1);
      this.rightBottom = Point.build(x2, y2);
      this.leftBottom = Point.build(x1, y2);
    }

    /**
     * 构造一个车牌框
     *
     * @param x1 左上角坐标X的值
     * @param y1 左上角坐标Y的值
     * @param x2 右下角坐标X的值
     * @param y2 右下角坐标Y的值
     */
    public static PlateBox build(float x1, float y1, float x2, float y2) {
      return new PlateBox((int) x1, (int) y1, (int) x2, (int) y2);
    }

    /**
     * 构造一个车牌框
     *
     * @param leftTop     左上角坐标值
     * @param rightTop    右上角坐标
     * @param rightBottom 右下角坐标
     * @param leftBottom  左下角坐标
     */
    public static PlateBox build(Point leftTop, Point rightTop, Point rightBottom,
        Point leftBottom) {
      return new PlateBox(leftTop, rightTop, rightBottom, leftBottom);
    }

    /**
     * x的最小坐标
     *
     * @return
     */
    public float x1() {
      return Math.min(Math.min(Math.min(leftTop.x, rightTop.x), rightBottom.x), leftBottom.x);
    }

    /**
     * y的最小坐标
     *
     * @return
     */
    public float y1() {
      return Math.min(Math.min(Math.min(leftTop.y, rightTop.y), rightBottom.y), leftBottom.y);
    }

    /**
     * x的最大坐标
     *
     * @return
     */
    public float x2() {
      return Math.max(Math.max(Math.max(leftTop.x, rightTop.x), rightBottom.x), leftBottom.x);
    }

    /**
     * y的最大坐标
     *
     * @return
     */
    public float y2() {
      return Math.max(Math.max(Math.max(leftTop.y, rightTop.y), rightBottom.y), leftBottom.y);
    }

    /**
     * 判断当前的车牌框是否是标准的车牌框，即非旋转后的车牌框。
     *
     * @return 否是标准的车牌框
     */
    public boolean normal() {
      if ((int) leftTop.x == (int) leftBottom.x && (int) leftTop.y == (int) rightTop.y) {
        if ((int) rightBottom.x == (int) rightTop.x && (int) rightBottom.y == (int) leftBottom.y) {
          return true;
        }
      }
      return false;
    }

    /**
     * 获取宽度
     *
     * @return
     */
    public float width() {
      return (float) Math.sqrt(
          Math.pow((rightTop.x - leftTop.x), 2) + Math.pow((rightTop.y - leftTop.y), 2));
    }

    /**
     * 获取高度
     *
     * @return
     */
    public float height() {
      return (float) Math.sqrt(
          Math.pow((rightTop.x - rightBottom.x), 2) + Math.pow((rightTop.y - rightBottom.y), 2));
    }

    /**
     * 获取面积
     *
     * @return
     */
    public float area() {
      return this.width() * this.height();
    }

    /**
     * 中心点坐标
     *
     * @return
     */
    public Point center() {
      return Point.build((rightTop.x + leftBottom.x) / 2, (rightTop.y + leftBottom.y) / 2);
    }

    /**
     * 对车牌框进行旋转对应的角度
     *
     * @param angle 旋转角
     * @return
     */
    public PlateBox rotate(float angle) {
      Point center = this.center();
      Point rPoint1 = this.leftTop.rotation(center, angle);
      Point rPoint2 = this.rightTop.rotation(center, angle);
      Point rPoint3 = this.rightBottom.rotation(center, angle);
      Point rPoint4 = this.leftBottom.rotation(center, angle);
      return new PlateBox(rPoint1, rPoint2, rPoint3, rPoint4);
    }

    /**
     * 中心缩放
     *
     * @param scale
     * @return
     */
    public PlateBox scaling(float scale) {
      //p1-p3
      float length_p1_p3 = leftTop.distance(rightBottom);
      float x_diff_p1_p3 = leftTop.x - rightBottom.x;
      float y_diff_p1_p3 = leftTop.y - rightBottom.y;
      float change_p1_p3 = length_p1_p3 * (1 - scale);
      float change_x_p1_p3 = change_p1_p3 * x_diff_p1_p3 / length_p1_p3 / 2;
      float change_y_p1_p3 = change_p1_p3 * y_diff_p1_p3 / length_p1_p3 / 2;
      //p2-p4
      float length_p2_p4 = rightTop.distance(leftBottom);
      float x_diff_p2_p4 = rightTop.x - leftBottom.x;
      float y_diff_p2_p4 = rightTop.y - leftBottom.y;
      float change_p2_p4 = length_p2_p4 * (1 - scale);
      float change_x_p2_p4 = change_p2_p4 * x_diff_p2_p4 / length_p2_p4 / 2;
      float change_y_p2_p4 = change_p2_p4 * y_diff_p2_p4 / length_p2_p4 / 2;
      //构造车牌框
      return new PlateBox(
          new Point(leftTop.x - change_x_p1_p3, leftTop.y - change_y_p1_p3),
          new Point(rightTop.x - change_x_p2_p4, rightTop.y - change_y_p2_p4),
          new Point(rightBottom.x + change_x_p1_p3, rightBottom.y + change_y_p1_p3),
          new Point(leftBottom.x + change_x_p2_p4, leftBottom.y + change_y_p2_p4)
      );
    }

    /**
     * 将框进行平移
     *
     * @param top    向上移动的像素点数
     * @param bottom 向下移动的像素点数
     * @param left   向左移动的像素点数
     * @param right  向右移动的像素点数
     * @return 平移后的框
     */
    public PlateBox move(int left, int right, int top, int bottom) {
      return new PlateBox(
          new Point(leftTop.x - left + right, leftTop.y - top + bottom),
          new Point(rightTop.x - left + right, rightTop.y - top + bottom),
          new Point(rightBottom.x - left + right, rightBottom.y - top + bottom),
          new Point(leftBottom.x - left + right, leftBottom.y - top + bottom)
      );
    }

    /**
     * 转换为数组
     *
     * @return
     */
    public Point[] toArray() {
      return new Point[]{leftTop, rightTop, rightBottom, leftBottom};
    }

  }

  public static class ParseInfo implements Serializable {

    /**
     * 当前图片的base64编码值
     **/
    public String image;
    /**
     * 车牌文本信息
     **/
    public String plateNo;
    /**
     * 车牌的颜色信息
     **/
    public String plateColor;
    /**
     * 车牌颜色的分数
     **/
    public float colorScore;

    private ParseInfo(String image, String plateNo, String plateColor, float colorScore) {
      this.image = image;
      this.plateNo = plateNo;
      this.plateColor = plateColor;
      this.colorScore = colorScore;
    }

    public static ParseInfo build(String image, String plateNo, String plateColor,
        float colorScore) {
      return new ParseInfo(image, plateNo, plateColor, colorScore);
    }
  }
}
