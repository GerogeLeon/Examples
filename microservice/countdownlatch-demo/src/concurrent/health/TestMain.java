package concurrent.health;

public class TestMain {
    public static void main(String[] args) {
        boolean result = false;
        try {
            result = ApplicationStartupUtil.checkExternalServices();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("External services validation completed !! Result was :: " + result);
    }

}
