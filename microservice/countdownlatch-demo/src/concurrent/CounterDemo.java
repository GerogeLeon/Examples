package concurrent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Java 并发实践- ConcurrentHashMap 与 CAS
 * API调用次数统计
 * 涉及概念： 多线程/线程池/ConcurrentHashMap/CountDownLatch
 */
public class CounterDemo {
    private final Map<String, Long> urlCounter = new ConcurrentHashMap<>();



    /**
     * 接口调用次数，此方法存在问题,ConcurrentHashMap的原子方法是同步的，但increase方法没有同步
     * @param url
     * @return
     */
    public long increase(String url) {
        Long oldValue=urlCounter.get(url);
        Long newValue=(oldValue==null)?1l:oldValue+1;
        urlCounter.put(url,newValue);
        return newValue;
    }

    /**
     * CAS 乐观锁/自旋
     * @param url
     * @return
     */
    public long increase2(String url){
        Long oldValue,newValue;
        while(true){
            oldValue=urlCounter.get(url);
            if(oldValue==null){
                newValue=1l;
                //初始化成功，退出循环
                if(urlCounter.putIfAbsent(url,1l)==null)
                    break;
                //如果初始化失败，说明其他线程已经初始化了
            }else{
                newValue=oldValue+1;
                //+1成功，退出循环
                if(urlCounter.replace(url,oldValue,newValue)){
                    break;
                    //如果+1失败，则说明其他线程已经修改过了旧值
                }
            }
        }
        return newValue;
    }

    //第三种方式，使用Google的Guava项目解决
    //AtomicLongMap<String> urlCounter3 = AtomicLongMap.create(); //线程安全，支持并发
    /*public long increase3(String url){
        return urlCounter3.incrementAndGet(url);
    }*/

    //第四种方法
   /* Map<String, Integer> map2 = new HashMap<String, Integer>(); //线程不安全
    ReentrantReadWriteLock lock = new ReentrantReadWriteLock(); //为map2增加并发锁


    //对map2添加写锁，可以解决线程并发问题
                    lock.writeLock().lock();
                    try{
        if(map2.containsKey(key)){
            map2.put(key, map2.get(key)+1);
        }else{
            map2.put(key, 1);
        }
    }catch(Exception ex){
        ex.printStackTrace();
    }finally{
        lock.writeLock().unlock();
    }*/


    //获取调用次数
    public long getCount(String url){
        return urlCounter.get(url);
    }

    public static void main(String[] args) {
        ExecutorService executorService= Executors.newFixedThreadPool(10);
        final CounterDemo counterDemo=new CounterDemo();
        int callTime=100000;
        final String url="http://localhost:8082/test";
        CountDownLatch countDownLatch=new CountDownLatch(callTime);

        //模拟并发情况下的接口调用统计
        for (int i = 0; i < callTime; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    counterDemo.increase(url);
                    countDownLatch.countDown();
                }
            });
        }

        try{
            countDownLatch.await();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        executorService.shutdown();

        //等待所有线程统计完成后输出调用次数
        System.out.println("调用次数："+counterDemo.getCount(url));

    }
}
