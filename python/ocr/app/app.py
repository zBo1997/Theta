import threading

from python.ocr.route.demo import server
from python.ocr.register.server import service_register,service_beat


# 发布http服务，并且注册到nocos
if __name__ == "__main__":
    service_register()
    # 5秒以后，异步执行service_beat()方法
    threading.Timer(5, service_beat).start()
    server.run(port=8085, debug=True)
