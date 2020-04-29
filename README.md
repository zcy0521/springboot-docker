# SpringBoot Docker

## Usage

[Spring Boot with Docker](https://spring.io/guides/gs/spring-boot-docker/)

[Topical Guide on Spring Boot with Docker](https://spring.io/guides/topicals/spring-boot-docker)

- 编写 `Dockerfile` 文件

```dockerfile
# syntax=docker/dockerfile:experimental
FROM openjdk:8-jdk-alpine as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM openjdk:8-jre-alpine

RUN addgroup -S app && adduser -S app -G app
USER app

VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","com.sample.springboot.build.docker.Application"]
```

- 生成镜像

```shell script
DOCKER_BUILDKIT=1 docker build -t [IMAGE_NAME] .
```

- 运行

```shell script
docker-compose up -d
```

- 访问

http://localhost:8081
http://localhost:8082

## Docker

### Install

- [Debian](https://docs.docker.com/install/linux/docker-ce/debian)

```shell script
sudo apt-get update
sudo apt-get install apt-transport-https ca-certificates curl gnupg2 software-properties-common
curl -fsSL https://download.docker.com/linux/debian/gpg | sudo apt-key add -
sudo apt-key fingerprint 0EBFCD88
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/debian $(lsb_release -cs) stable"
sudo apt-get update
sudo apt-get install docker-ce docker-ce-cli containerd.io
sudo docker run hello-world
```
  
### Docker Compose

- [Linux](https://docs.docker.com/compose/install/#install-compose-on-linux-systems)

```shell script
sudo curl -L "https://github.com/docker/compose/releases/download/1.25.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
```

## Nginx代理

- 新建配置文件

```shell script
nano [DOMAIN_NAME].conf
upstream [APP_NAME] {
    server host.docker.internal:[TOMCAT_PORT];
    server host.docker.internal:[TOMCAT_PORT];
}

server {
    listen       [NGINX_PORT];
    server_name  [DOMAIN_NAME];

    location / {
        proxy_pass http://[APP_NAME];
    }
}
```

- 重启nginx

```shell script
cp app.conf nginx-docker/conf.d
docker restart nginx
```

- 修改`hosts`

```
127.0.0.1 [DOMAIN_NAME]
```

- 访问

http://DOMAIN_NAME
