from locust import HttpUser, task, between


class ApiUser(HttpUser):
    # 每秒启动2个用户，等待时间为0.5至1秒之间
    wait_time = between(0.5, 1)

    @task
    def get_all_roles(self):
        form_data = {'userId': '1'}
        with self.client.get(
                "/userInfo/query",
                # 响应时间不能超过3秒
                timeout=3,
                # 设置运行为失败
                catch_response=True,
                params=form_data) as response:
            if response.status_code == 200:
                response.success()
            else:
                response.failure("请求失败，状态码：{}".format(response.status_code))


def on_start(self):
    # 结束
    pass
