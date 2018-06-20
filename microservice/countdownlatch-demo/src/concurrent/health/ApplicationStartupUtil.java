package concurrent.health;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ApplicationStartupUtil {
    //list of service checker
    private static List<BaseHealthChecker> checkers;

    //this latch will be used to wait on
    private static CountDownLatch countDownLatch;

    //singleton
    private ApplicationStartupUtil() {

    }

    private static ApplicationStartupUtil applicationStartupUtil = new ApplicationStartupUtil();

    public static ApplicationStartupUtil getInstance() {
        return applicationStartupUtil;
    }

    public static boolean checkExternalServices() throws InterruptedException {
        //init the latch with the number of service checks
        countDownLatch = new CountDownLatch(3);

        //add all service checks into the list
        checkers = new ArrayList<>();
        checkers.add(new DataBaseHealthChecker(countDownLatch));
        checkers.add(new UserHealthChecker(countDownLatch));
        checkers.add(new FileHealthChecker(countDownLatch));

        //start service checks using executor framework
        ExecutorService executor = Executors.newFixedThreadPool(checkers.size());
        for (BaseHealthChecker checker : checkers) {
            executor.execute(checker);
        }

        //now wait all services checked
        countDownLatch.await();

        //service checkers are finished and now proceed startup
        for (BaseHealthChecker checker : checkers) {
            if (!checker.isServiceUp()) {
                return false;
            }
        }
        return true;


    }
}
