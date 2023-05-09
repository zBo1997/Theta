from paddleocr import PaddleOCR
from python.ocr.service.pic_process import Morph_Distinguish


def open_file(file_path):
    # 获取车牌识别的范围
    display = Morph_Distinguish(file_path)
    if display is None:
        print("没有提取到车牌")
        return
    ocr = PaddleOCR(use_angle_cls=True, lang="ch")
    print(file_path)
    # 输出结果保存路径
    result = ocr.ocr(display, cls=True)
    print(result)
    return result


