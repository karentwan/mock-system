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

- MEM-内存存储模式

启动命令如下：
```shell
docker run -e mode=MEM -p 8079:8079 mock
```

MEM模式表示可以通过接口配置模板，配置的模板将存储在内存里面。
配置接口如下

```shell
POST http://localhost:8079/config

{
    "api": "/test",
    "headers": {
        "Content-Type": "text/plain",
        "cust": "aaa"
    }, 
    "template": "{\"a\": \"wan\"}"
}
```
其中api表示接口名称, headers表示响应头(可选), template表示模板响应的内容。

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

该模式下，系统在接收到请求的时候，会去模板目录下面寻找对应的文件配置(文件配置和调接口配置的内容格式是一样的)。

模板文件必须以`.json`格式结尾，文件名为接口名称(`/`被替换为_)，举个例子，接口/test/a/b,那么对应的文件名为`test_a_b.json`。


### 模板中预制的变量
模板中有一些预制的变量：header、body、F, 分别表示http请求头、请求体、可以使用的自定义函数。
举个例子，在我们渲染的模板中，有些内容是取自请求头、有些则是取自请求体、还有随机生成的数字，那么配置模板可以这样写：
```shell

{
  "api": "/test",
  "headers": {
    "content-type": "application/json"
  },
  "template": "<#if header.token??> {\"hello\": \"wan\", \"param\": \"${body.a}\", \"token\": \"${header.token}\", \"content_type\": \"${header.contentType}\"} <#else> {\"hello\": \"no token\", \"param\": \"${body.a}\", \"random\": \"${F.random.nextInt()}\"} </#if>"
}

```
仔细看template字段的内容，上面语法的意思是，如果header(发送请求时的http请求头)里面含有token则响应:
```shell

{
    "hello": "wan",
    "param": "${body.a}",
    "token": "${header.token}",
    "content_type": "${header.contentType}"
}

```
这上面的param取自请求体里面的a字段，content_type取自请求头里面的content-type字段。

如果header里面不含有token则响应:

```shell

{
    "hello": "no token",
    "param": "${body.a}",
    "random": "${F.random.nextInt()}"
}

```
这上面的param取自请求体里面的a字段，random取自随机生成的数字，而F.random就是Java里面的Random类, 用法跟它一样。

发送请求：
```shell
POST http://localhost:8079/test

{
  "a": "123"
}

```

### 插件系统
为了增强请求和响应的处理，系统还引入了一套插件系统体系，插件系统的配置是可插拔式的，下面给出一个修改http状态码的插件例子。

插件系统的配置
```shell

POST http://localhost:8079/config

{
    "api": "/test",
    "headers": {
      "content-type": "application/json"
    },
    "template": "<#if header.token??> {\"hello\": \"wan\", \"param\": \"${body.a}\", \"token\": \"${header.token}\", \"content_type\": \"${header.contentType}\"} <#else> {\"hello\": \"no token\", \"param\": \"${body.a}\", \"random\": \"${F.random.nextInt()}\"} </#if>",
    "plugins":
    [
        {
            "name": "HttpStatus",
            "config": {"status": 400 }
        }
    ]
}

```

上面是http状态码的插件配置，使用上面的配置，当我们访问`http://localhost:8079/test` 时，返回的http状态码为400，这可以在测试我们的系统时模拟一些异常情况，方便我们测试。

上面的模板配置中，`plugins`是配置的插件列表，该参数配置可以配置多个插件，多个插件会依次生效。

除了上面的插件，系统还内置一些其他的插件，如下：
**延时插件**
```shell
POST http://localhost:8079/config

{
    "api": "/test",
    "headers": {
      "content-type": "application/text"
    },
    "template": "hello, world",
    "plugins":
    [
        {
            "name": "DelayResponse",
            "config": {
                "delay_time": 10,
                "unit": "SECONDS"
            }
        }
    ]
}
```

**回调插件**
注：回调插件可以自定义拦截器, 例如加签、验签等功能可以写在拦截器里面
```shell
POST http://localhost:8079/config

{
    "api": "/test",
    "headers":
    {
        "content-type": "application/text"
    },
    "template": "hello, world",
    "plugins":
    [
        {
            "name": "Callback",
            "config":
            {
                "url": "http://thirdpart:8080/callback",
                "interval_time": 10,
                "unit": "SECONDS",
                "method": "POST",
                "headers":
                {
                    "content-type": "application/json"
                },
                "body": "{\"hello\": \"world\"}",
                "interceptor":
                {
                    "WeXinSignInterceptor":
                    {
                        "config": "key"
                    }
                }
            }
        }
    ]
}
```
上面回调拦截器后面配置的json就是POST请求的请求体, 除了json还可以使用其他的数据格式。

**路由插件**

当需要根据请求参数响应不同的响应体时, 可以配置路由插件, 该路由插件根据谓词(predicate)来决定请求走哪一个路由，而且路由插件具备独立的插件列表，配置方法如下：
```shell
{
  "api": "/test",
  "routes": [
    {
      "id": "id1",
      "template": {
        "hello": "wan"
      },
      "predicates": [
        {
          "name": "body",
          "args": {
            "name": "name",
            "value": "wan"
          }
        }
      ],
      "plugins": [
        {
          "name": "HttpStatus",
          "config": {
            "status": 400
          }
        }
      ]
    },
    {
      "id": "id2",
      "template": {
        "hello": "帅哥"
      },
      "predicates": [
        {
          "name": "body",
          "args": {
            "name": "name",
            "value": "wu"
          }
        }
      ]
    }
  ]
}
```
这上面配置有两个路由, 第一个路由条件是请求体里面含有name字段，且值为wan, 第二个路由条件是请求体里面含有name字段且值为wu,当满足第一个路由条件时，
响应第一个路由里面配置的模板，且返回状态码为404,因为第一个路由配置了404插件。
目前只实现了一个判断匹配请求体里面内容的谓词(predicate)。

#### 自定义插件规范
如果有自定义插件的需求，需要实现`cn.karent.filter.plugin.Plugin` 接口, 并重写它的方法：
> void doProcess(Request request, Response response, PluginChain chain) throws ServletException, IOException; 


如果只想要改变请求、响应二者之一，可以继承`cn.karent.filter.plugin.PluginAdapter`接口，并实现里面的`processRequest(Request)`方法或者`processResponse(Response)`方法。

如果自定义插件有自己的参数（如上面配置Http状态码），那么需要实现`cn.karent.common.Configurable`接口，并重写它的方法：
>  void configure0(C c);

该方法会将参数传递给插件，具体的例子可以参考：`cn.karent.plugin.HttpStatusPlugin`类的实现。


**注意：自定义的插件需要加上SpringBoot的@Component注解，否则插件系统的配置将无法生效。**

### 打印日志
正常情况只会打印请求和响应信息，如果需要打印请求头信息，可以通过在参数后加`log_level`参数来调整日志级别:
```shell
POST http://localhost:8079/test?log_level=base

{
  "a": "123"
}
```
如上是使用方法，如果log_level设为base则只会打印请求头，如果log_level设为body(默认不设就是body)，则会打印请求行和请求体, 如果将log_level设为full, 则会将请求行/请求头/请求体全部打印