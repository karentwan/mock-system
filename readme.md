### 使用docker部署

先编译出服务的jar包，可以使用idea的package指令，或者使用maven命令，如下:

```shell

mvn package

```

构建docker镜像
```shell
# 进入工程目录，执行
docker build -t mock .
```

### 服务启动

服务的启动有两种模式, 分别为：

- STRING-配置模式

启动命令如下：
```shell
docker run -e mode=STRING -p 8079:8079 mock
```

STRING模式表示可以通过接口配置模板，配置接口如下

```shell
POST http://localhost:8079/template/config

{
    "api": "/test",
    "headers": "{\"Content-Type\": \"text/plain\", \"cust\": \"aaa\"}", 
    "template": "{\"a\": \"wan\"}"
}
```
其中api表示接口名称, headers表示响应头, 可以不配, template表示模板响应的内容。

配置完后访问：
```shell
POST http://localhost:8079/test

{
    "a": "wan"
}


# 响应结果

{
    "a": "wan"
}
```

- FILE -文件模式

启动命令如下：

```shell
docker run -e mode=FILE -e path=/app/data -p 8079:8079 mock
```

FILE模式表示响应的模板放在/app/data目录下面。

该模式下，不能指定响应头, 响应体只能是JSON格式。

模板文件必须以`.ftl`格式结尾，文件名为接口名称(`/`被替换为_)，举个例子，接口/test/a/b,那么对应的文件名为`test_a_b.ftl`。