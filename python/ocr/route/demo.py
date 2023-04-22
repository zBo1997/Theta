from flask import Flask, jsonify, request
from werkzeug.utils import secure_filename
from python.ocr.service.ocr import open_file

import os

# Flask初始化参数尽量使用你的包名，这个初始化方式是官方推荐的，官方解释：http://flask.pocoo.org/docs/0.12/api/#flask.Flask
server = Flask(__name__)
# 处理乱码
server.config['JSON_AS_ASCII'] = False


@server.route('/simulation/analysis/<name>', methods=['get'])
def demo_restful_request(name):
    # 处理业务逻辑
    # name = request.args['name']
    result = {"code": "200", "msg": "SUCCES", "data": {"name": name, "age": 25, "job": "python"}}
    return jsonify(result)


@server.route('/simulation/analysis', methods=['get'])
def demo_rest_get_request():
    # 处理业务逻辑
    name = request.args['name']
    result = {"code": "200", "msg": "SUCCES", "data": {"name": name, "age": 25, "job": "python"}}
    return jsonify(result)



@server.route('/upload/File', methods=['post'])
def demo_rest_post_request():
    f = request.files['file']
    basepath = os.path.dirname(__file__)
    upload_path = os.path.join(basepath, 'static',secure_filename(f.filename))
    f.save(upload_path)
    open_file(upload_path)
    return jsonify(f.filename)

