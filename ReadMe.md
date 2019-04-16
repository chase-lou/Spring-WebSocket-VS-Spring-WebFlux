#WebSocket+服务间调用，demo，为了性能测试

 *Spring Mvc* + *Spring WebSocket* VS *Spring WebFlux*

个人理解可以简单的当做tomcat对比netty

**结果**:
![对比结果](https://github.com/chase-lou/Spring-WebSocket-VS-Spring-WebFlux/blob/master/%E5%AF%B9%E6%AF%94%E7%BB%93%E6%9E%9C.png?raw=true)

**逻辑：**
1、每个服务启动时分别在redis添加一条数据，Key：Id（启动时生成），Value：IP+Port，
2、当收到一条WebSocket消息的时候，获取所有的服务数据，分别请求Http接口，发送此消息
3、每个服务监听到Http请求后，向自己服务上所有的WebSocket连接发送该Http请求中包含的消息

**测试**
启动2个实例，分别测试50、100、150个WebSocket连接，50、100、150个线程同时发送（发送间隔为0.3s）、获取消息，线程启动的速度是一样的，分别是15秒启动50个线程，30秒启动100个线程，45秒启动150个线程

**测试结果**
测试结果暂未分析。tomcat和netty都是用的默认配置。
