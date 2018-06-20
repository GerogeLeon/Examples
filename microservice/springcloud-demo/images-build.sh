#!/bin/bash

# 编译所有的代码，生成docker 镜像
function progress(){
    local GREEN CLEAN
    GREEN='\033[0;32m'
    CLEAN='\033[0m'
    printf "\n${GREEN}$@  ${CLEAN}\n" >&2
}

set -e

# 镜像仓库前缀
REGPREFIX=127.0.0.1:5000/billjiang
TOTAL=10

#discovery server
progress "Building discovery-server(1/${TOTAL}) jar file ..."
cd common/discovery
mvn clean package -DskipTests
progress "Building discovery(eureka server)(1/${TOTAL}) docker image ..."
docker tag $(docker build -t ${REGPREFIX}/discovery -q .) ${REGPREFIX}/discovery:$(date -u "+%Y%m%d-%H%M%S")
cd -

#config server
progress "Building config-server(2/${TOTAL}) jar file ..."
cd common/configserver
mvn clean package -DskipTests
progress "Building config server(2/${TOTAL}) docker image ..."
docker tag $(docker build -t ${REGPREFIX}/configserver -q .) ${REGPREFIX}/configserver:$(date -u "+%Y%m%d-%H%M%S")
cd -

#gateway
progress "Building gateway-zuul(3/${TOTAL}) jar file ..."
cd common/gateway
mvn clean package -DskipTests
progress "Building gateway-zuul(3/${TOTAL}) docker image ..."
docker tag $(docker build -t ${REGPREFIX}/gateway -q .) ${REGPREFIX}/gateway:$(date -u "+%Y%m%d-%H%M%S")
cd -

#track sleuth+zipkin
progress "Building track(sleuth+zipkin)(4/${TOTAL}) jar file ..."
cd common/track
mvn clean package -DskipTests
progress "Building track by sleuth+zipkin(4/${TOTAL}) docker image ..."
docker tag $(docker build -t ${REGPREFIX}/track -q .) ${REGPREFIX}/track:$(date -u "+%Y%m%d-%H%M%S")
cd -

#hystrix-dashboard
progress "Building hystrix-dashboard(5/${TOTAL}) jar file ..."
cd common/hystrix-dashboard
mvn clean package -DskipTests
progress "Building hystrix-dashboard(5/${TOTAL}) docker image ..."
docker tag $(docker build -t ${REGPREFIX}/hystrix-dashboard -q .) ${REGPREFIX}/hystrix-dashboard:$(date -u "+%Y%m%d-%H%M%S")
cd -

#hystrix-turbine-mq
progress "Buildinhg hystrix-turbine-mq(6/${TOTAL}) jar file ..."
cd common/hystrix-turbine-mq
mvn clean package -DskipTests
progress "Building hystrix-turbine-mq(6/${TOTAL}) docker image ..."
docker tag $(docker build -t ${REGPREFIX}/hystrix-turbine-mq -q .) ${REGPREFIX}/hystrix-turbine-mq:$(date -u "+%Y%m%d-%H%M%S")
cd -

#hello
progress "Buildinhg hello service(7/${TOTAL}) jar file ..."
cd service/hello
mvn clean package -DskipTests
progress "Building hello service(7/${TOTAL}) docker image ..."
docker tag $(docker build -t ${REGPREFIX}/hello -q .) ${REGPREFIX}/hello:$(date -u "+%Y%m%d-%H%M%S")
cd -


#world
progress "Buildinhg world service(8/${TOTAL}) jar file ..."
cd service/world
mvn clean package -DskipTests
progress "Building world service(8/${TOTAL}) docker image ..."
docker tag $(docker build -t ${REGPREFIX}/world -q .) ${REGPREFIX}/world:$(date -u "+%Y%m%d-%H%M%S")
cd -

#helloworld
progress "Buildinhg helloworld service(9/${TOTAL}) jar file ..."
cd service/helloworld
mvn clean package -DskipTests
progress "Building helloworld service(9/${TOTAL}) docker image ..."
docker tag $(docker build -t ${REGPREFIX}/helloworld -q .) ${REGPREFIX}/helloworld:$(date -u "+%Y%m%d-%H%M%S")
cd -

#helloworld-feign
progress "Buildinhg helloworld-feign service(10/${TOTAL}) jar file ..."
cd service/helloworld-feign
mvn clean package -DskipTests
progress "Building helloworld-feign service(10/${TOTAL}) docker image ..."
docker tag $(docker build -t ${REGPREFIX}/helloworld-feign -q .) ${REGPREFIX}/helloworld-feign:$(date -u "+%Y%m%d-%H%M%S")
cd -
