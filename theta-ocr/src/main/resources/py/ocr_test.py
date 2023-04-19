import pytesseract
from PIL import Image
text = pytesseract.image_to_string(Image.open(r"./未命名1681892749.png"))
print(text)