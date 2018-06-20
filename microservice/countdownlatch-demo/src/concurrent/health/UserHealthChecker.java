package concurrent.health;

import java.util.concurrent.CountDownLatch;

public class UserHealthChecker extends BaseHealthChecker {
    public UserHealthChecker(CountDownLatch countDownLatch) {
        super("user service", countDownLatch);
    }

    @Override
    public void verifySerivce() {
        System.out.println("Checking " + this.getServiceName());
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this.getServiceName() + " is UP");
    }
}
