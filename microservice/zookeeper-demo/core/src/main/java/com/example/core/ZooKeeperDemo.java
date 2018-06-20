package com.example.core;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

public class ZooKeeperDemo {

    private static final String CONNECTION_STRING="127.0.0.1:2181";
    private static final int SESSION_TIMEOUT=5000;
    private static CountDownLatch latch=new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        ZooKeeper zooKeeper=new ZooKeeper(CONNECTION_STRING, SESSION_TIMEOUT, watchedEvent -> {
            if(watchedEvent.getState()== Watcher.Event.KeeperState.SyncConnected){
                latch.countDown();
            }
        });
        latch.await();
        System.out.println(zooKeeper);
    }

}
