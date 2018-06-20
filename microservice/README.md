# microservice
该项目主要包含了一系列基于spring cloud+docker的微服务项目，是我的简书微服务系列文章配套的示例：

我的简书：[billJiang的简书地址](http://www.jianshu.com/u/1129e8da7a07)
Github地址：[micoreservice](https://github.com/bill1012/microservice)

# countdownlatch-demo
多线程在微服务API统计和健康检查中的使用,重点是countdownlatch的使用

相关文章：
- [多线程在微服务API统计和健康检查中的使用](http://www.jianshu.com/p/5bb0ebde9800)
# boot-admin-demo
开源框架spring-boot-admin的配置和使用，提供了服务注册/监控/路由跟踪的功能。

相关文章：
- [Spring Boot Admin使用及心跳检测原理](http://www.jianshu.com/p/1170f4593638)

# consul-demo
使用consul作为服务发现组件示例

相关文章：
- [Spring Cloud构建微服务架构：Consul服务注册与发现](http://www.jianshu.com/p/6ee1fe79e959)

# eureka-demo
eureka作为微服务注册和发现组件示例，包含`eureka-client`和`eureka-server`

相关文章：
- [Spring Cloud构建微服务架构：Eureka服务注册与发现](http://www.jianshu.com/p/1170f4593638)

# zookeeper-demo
使用zookeeper作为服务注册发现组件示例，包含服务`client`、`client2`和操作zookeeper的项目`core`

相关文章：
- [基于ZooKeeper服务注册实现](http://www.jianshu.com/p/0dfac0ad266f)


# node-zookeeper-demo
使用nodejs作为API网关，使用随机负载的方式从zookeeper集群中获取服务地址，并代理服务请求

相关文章：
-  [基于ZooKeeper服务注册实现](http://www.jianshu.com/p/0dfac0ad266f)
-  [基于nodejs+zookeeper服务发现](http://www.jianshu.com/p/a6b01048d94f)

# springcloud-demo
一个相对完整的微服务架构实例，包含如下子项目：
- 服务注册 discovery：eureka
- 服务网关 gateway： zuul
- 断路器 hystrix：hystrix
- 链路跟踪 trace: sleuth+zipkin
- 微服务 hello
- 微服务 world
- 微服务 helloworld： restTemplate+Ribbon 
- 微服务 helloworldfeign : feign

相关文章：
- [微服务之间的调用（Ribbon与Feign）](http://www.jianshu.com/p/7ca91139dca5)
- [服务注册Eureka原理及集群配置](http://www.jianshu.com/p/ee14bbee732b)
- [Ribbon负载均衡策略与自定义配置](http://www.jianshu.com/p/768851b9e298)
- [自定义feign配置与服务调用的安全验证](http://www.jianshu.com/p/755b15ff0249)
- [断路器hystrix原理及使用](http://www.jianshu.com/p/53e109bf5c54)
- [Hystrix监控的配置详解](http://www.jianshu.com/p/b7b20fc09ca9)
- [Spring Cloud Config配置详解](http://www.jianshu.com/p/e48de30aab76)
- [使用sleuth实现微服务跟踪](http://www.jianshu.com/p/5df2e83d0ef8)
