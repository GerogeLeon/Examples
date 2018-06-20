#!/bin/bash

# 将本地镜像推送到私有仓库
set -e

# 镜像仓库前缀
REGPREFIX=127.0.0.1:5000/billjiang

docker push ${REGPREFIX}/discovery

#docker push ${docker images -q}





