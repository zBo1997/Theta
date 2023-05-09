package com.momo.theta.domain;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.opencv.core.CvType;
import org.opencv.core.Mat;


public class DrawImage {

  private BufferedImage image;

  private DrawImage(BufferedImage image) {
    this.image = image;
  }

  public static DrawImage build(String image) {
    try {
      return build(ImageIO.read(new File(image)));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static DrawImage build(BufferedImage image) {
    return new DrawImage(image);
  }

  private static int getLength(String text) {
    int textLength = text.length();
    int length = textLength;
    for (int i = 0; i < textLength; i++) {
      if (String.valueOf(text.charAt(i)).getBytes().length > 1) {
        length++;
      }
    }
    return (length % 2 == 0) ? length / 2 : length / 2 + 1;
  }

  public DrawImage drawRect(Rect rect, int lineWidth, Color color) {
    Graphics2D g = (Graphics2D) image.getGraphics();
    g.setColor(color);
    g.setStroke(new BasicStroke(lineWidth));
    g.drawRect(rect.x, rect.y, rect.width, rect.height);
    return this;
  }

  public DrawImage drawLine(Point point1, Point point2, int lineWidth, Color color) {
    Graphics2D g = (Graphics2D) image.getGraphics();
    g.setColor(color);
    g.setStroke(new BasicStroke(lineWidth));
    g.drawLine(point1.x, point1.y, point2.x, point2.y);
    return this;
  }

  public DrawImage drawText(String text, Point point, int fontSize, Color color) {
    Graphics2D g = image.createGraphics();
    g.setColor(color);
    g.setFont(new Font("微软雅黑", Font.PLAIN, fontSize));
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1));
    int width_1 = fontSize * getLength(text);
    int height_1 = fontSize;
    int widthDiff = image.getWidth() - width_1;
    int heightDiff = image.getHeight() - height_1;
    if (point.x < 0) {
      point.x = widthDiff / 2;
    } else if (point.x > widthDiff) {
      point.x = widthDiff;
    }
    if (point.y < 0) {
      point.y = heightDiff / 2;
    } else if (point.y > heightDiff) {
      point.y = heightDiff;
    }
    g.drawString(text, point.x, point.y + height_1);
    return this;
  }

  public Mat toMat() {
    try {
      if (image.getType() != BufferedImage.TYPE_3BYTE_BGR) {
        BufferedImage temp = new BufferedImage(image.getWidth(), image.getHeight(),
            BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = temp.createGraphics();
        try {
          g.setComposite(AlphaComposite.Src);
          g.drawImage(image, 0, 0, null);
        } finally {
          g.dispose();
        }
        image = temp;
      }
      byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
      Mat mat = Mat.eye(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
      mat.put(0, 0, pixels);
      return mat;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static class Rect {

    public int x;
    public int y;
    public int width;
    public int height;

    public Rect(int x, int y, int width, int height) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
    }

  }

  public static class Point {

    public int x;
    public int y;

    public Point(int x, int y) {
      this.x = x;
      this.y = y;
    }
  }

}
