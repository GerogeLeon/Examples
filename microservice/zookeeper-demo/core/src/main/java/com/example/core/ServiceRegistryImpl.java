package com.example.core;

import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class ServiceRegistryImpl implements ServiceRegistry, Watcher {
    private static Logger logger = LoggerFactory.getLogger(ServiceRegistryImpl.class);

    private static CountDownLatch latch = new CountDownLatch(1);

    private ZooKeeper zk;

    private static final int SESSION_TIMEOUT = 5000;

    public ServiceRegistryImpl() {

    }

    public ServiceRegistryImpl(String zkServers) {
        try {
            zk = new ZooKeeper(zkServers, SESSION_TIMEOUT, this);
            latch.await();
            logger.debug("connected to zookeeper");
        } catch (Exception ex) {
            logger.error("create zookeeper client failure", ex);
        }
    }

    private static final String REGISTRY_PATH = "/registry";

    @Override
    public void register(String serviceName, String serviceAddress) {
        try {
            String registryPath = REGISTRY_PATH;
            if (zk.exists(registryPath, false) == null) {
                zk.create(registryPath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                logger.debug("create registry node:{}", registryPath);
            }
            //创建服务节点（持久节点）
            String servicePath = registryPath + "/" + serviceName;
            if (zk.exists(servicePath, false) == null) {
                zk.create(servicePath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                logger.debug("create service node:{}", servicePath);
            }
            //创建地址节点
            String addressPath = servicePath + "/address-";
            String addressNode = zk.create(addressPath, serviceAddress.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            logger.debug("create address node:{} => {}", addressNode, serviceAddress);
        } catch (Exception e) {
            logger.error("create node failure", e);
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected)
            latch.countDown();
    }
}
