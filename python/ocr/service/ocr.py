from paddleocr import PaddleOCR


def open_file(file_path):
    ocr = PaddleOCR(use_angle_cls=True, lang="ch")
    print(file_path)
    # 输出结果保存路径
    result = ocr.ocr(file_path, cls=True)
    return result
