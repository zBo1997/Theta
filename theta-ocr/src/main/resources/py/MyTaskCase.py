import random
from locust import TaskSet, HttpLocust, task, seq_task, between


# 定义一个任务类，这个类名称自己随便定义，类继承TaskSequence 或 TaskSet类，所以要从locust中，引入TaskeSequence或TaskSet
# 当类里面的任务请求有先后顺序时，继承TaskSequence类， 没有先后顺序，可以使用继承TaskSet类
class MyTaskCase(TaskSet):
    # 初始化方法，相当于 setup
    def on_start(self):
        pass

    # @task python中的装饰器，告诉下面的方法是一个任务，任务就可以是一个接口请求，
    # 这个装饰器和下面的方法被复制多次，改动一下，就能写出多个接口
    # 装饰器后面带上(数字)代表在所有任务中，执行比例
    # 要用这个装饰器，需要头部引入 从locust中，引入 task
    @task
    @seq_task(1)  # 装饰器，定义有执行顺序的任务，扩展中的数字，从小到大，代表先后执行顺序
    def regist_(self):  # 一个方法， 方法名称可以自己改
        url = '/erp/regist'  # 接口请求的URL地址
        self.headers = {"Content-Type": "application/json"}  # 定义请求头为类变量，这样其他任务也可以调用该变量
        self.user = "locust_" + str(random.randint(10000, 100000))
        self.pwd = '1234567890'
        data = {"name": self.user, "pwd": self.pwd}  # post请求的 请求体
        # 使用self.client发起请求，请求的方法根据接口实际选,
        # catch_response 值为True 允许为失败 ， name 设置任务标签名称   -----可选参数
        rsp = self.client.post(url, json=data, headers=self.headers, catch_response=True, name='api_regist')
        if rsp.status_code == 200:
            rsp.success()
        else:
            rsp.failure('regist_ 接口失败！')

    @task  # 装饰器，说明下面是一个任务
    @seq_task(2)  # 顺序任务装饰器，说明下面的任务，第二个执行
    def login_(self):
        url = '/erp/loginIn'  # 接口请求的URL地址
        data = {"name": self.user, "pwd": self.pwd}
        rsp = self.client.post(url, json=data, headers=self.headers,
                               catch_response=True)  # 使用self.client发起请求，请求的方法 选择post
        self.token = rsp.json()['token']    # 提取响应json 中的信息，定义为 类变量
        if rsp.status_code == 200 and rsp.json()['code'] == "200":
            rsp.success()
        else:
            rsp.failure('login_ 接口失败！')

    @task  # 装饰器，说明下面是一个任务
    @seq_task(3)  # 顺序任务装饰器，说明下面的任务，第三个执行
    def getuser_(self):
        url = '/erp/user'  # 接口请求的URL地址
        headers = {"Token": self.token}  # 引用上一个任务的 类变量值   实现参数关联
        rsp = self.client.get(url, headers=headers, catch_response=True)  # 使用self.client发起请求，请求的方法 选择 get
        if rsp.status_code == 200:
            rsp.success()
        else:
            rsp.failure('getuser_ 接口失败！')

    # 结束方法， 相当于teardown
    def on_stop(self):
        pass


# 定义一个运行类 继承HttpLocust类， 所以要从locust中引入 HttpLocust类
class UserRun(HttpLocust):
    task_set = MyTaskCase  # 定义固定的 task_set  指定前面的任务类名称
    wait_time = between(0.1, 3)  # 设置运行过程中间隔时间 需要从locust中 引入 between