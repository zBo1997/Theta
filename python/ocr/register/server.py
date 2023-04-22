import time

import requests


# nacos服务注册
def service_register():
    url = "http://192.168.55.76:8848/nacos/v1/ns/instance?serviceName=theta-ocr-server&ip=127.0.0.1&port=8085&namespaceId=59656ea3-0252-4b9c-b9d2-3ae3eab5f631"
    res = requests.post(url)

    print("向nacos注册中心，发起服务注册请求，注册响应状态： {}".format(res.status_code))


# 服务检测
def service_beat():
    while True:
        url = "http://192.168.55.76:8848/nacos/v1/ns/instance/beat?serviceName=theta-ocr-server&ip=127.0.0.1&port=8085&namespaceId=59656ea3-0252-4b9c-b9d2-3ae3eab5f631"
        res = requests.put(url)
        print("已注册服务，执行心跳服务，续期服务响应状态： {}".format(res.status_code))
        time.sleep(5)


