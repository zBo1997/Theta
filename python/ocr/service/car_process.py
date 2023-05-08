import cv2



if __name__ == '__main__':

    # 读取图像
    img = cv2.imread('test-2.jpeg')

    # 转换为灰度图像
    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

    # 对图像进行高斯模糊
    blur = cv2.GaussianBlur(gray, (5, 5), 0)

    # 边缘检测
    edges = cv2.Canny(blur, 100, 200)

    # 寻找车牌轮廓
    contours, hierarchy = cv2.findContours(edges, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)

    # 寻找符合条件的矩形轮廓
    plate_rect = None
    for cnt in contours:
        approx = cv2.approxPolyDP(cnt, 0.02*cv2.arcLength(cnt, True), True)
        if len(approx) == 4:
            x, y, w, h = cv2.boundingRect(approx)
            if w > 100 and h > 30 and w/h > 2.0 and w/h < 5.0:
                plate_rect = (x, y, w, h)
                break

    # 绘制车牌边框
    if plate_rect is not None:
        cv2.rectangle(img, (plate_rect[0], plate_rect[1]), (plate_rect[0]+plate_rect[2], plate_rect[1]+plate_rect[3]), (0, 255, 0), 2)

    # 显示结果
    cv2.imshow('Plate Detection', img)
    cv2.waitKey(0)
    cv2.destroyAllWindows()