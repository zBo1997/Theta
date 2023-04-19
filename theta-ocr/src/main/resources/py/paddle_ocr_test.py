from paddleocr import PaddleOCR
from paddleocr.tools.infer.utility import draw_ocr

ocr = PaddleOCR(use_angle_cls=True, lang="ch")
# 输入待识别图片路径
img_path = r"../../java/com/momo/theta/locust/paddle_ocr_test.png"
# 输出结果保存路径
result = ocr.ocr(img_path, cls=True)
print(result)
#for line in result:
#    print(line)

# from PIL import Image
# image = Image.open(img_path).convert('RGB')
# boxes = [line[0] for line in result]
# txts = [line[1][0] for line in result]
# scores = [line[1][1] for line in result]
# im_show = draw_ocr(image, boxes, txts, scores)
# im_show = Image.fromarray(im_show)
# im_show.show()
