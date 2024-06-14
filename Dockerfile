FROM openjdk:17-alpine

RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories && apk update && apk add busybox-extras

# 设置工作目录
WORKDIR /app

# 将当前目录中的JAR文件复制到镜像中的/app目录
COPY target/mock-system-1.0.0.jar /app/mock-system.jar

# 暴露应用生成的端口
EXPOSE 8079

# 设置环境变量
ENV mode='STRING'
ENV path="/app/data"


# 定义应用程序的启动命令
#cmd ["java", "-jar", "mock-system.jar"]
ENTRYPOINT ["sh","-c","java -Dtemplate.mode=${mode} -Dpath=template.template-path=${path} -jar mock-system.jar "]
