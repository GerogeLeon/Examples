package concurrent.health;

import java.util.concurrent.CountDownLatch;

public abstract class BaseHealthChecker implements Runnable {

    private CountDownLatch countDownLatch;

    private String serviceName;

    private boolean serviceUp;

    public BaseHealthChecker(String serviceName,CountDownLatch countDownLatch){
        super();
        this.serviceName=serviceName;
        this.countDownLatch=countDownLatch;
        this.serviceUp=false;
    }

    @Override
    public void run() {
        try{
            verifySerivce();
            serviceUp=true;
        }catch (Throwable t){
            t.printStackTrace(System.err);
            serviceUp=false;
        }finally {
            if(countDownLatch!=null)
                countDownLatch.countDown();
        }

    }


    public String getServiceName() {
        return serviceName;
    }

    public boolean isServiceUp() {
        return serviceUp;
    }

    //this method need to be implemented by all specific service checker
    public abstract void verifySerivce();

}
