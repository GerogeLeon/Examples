package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableCircuitBreaker
@RestController
public class HystrixApplication {

    @Bean
    public MyService myService() {
        return new MyService();
    }

    @Autowired
    private MyService myService;

    @RequestMapping("/")
    public String ok() {
        return myService.ok();
    }

    @RequestMapping("/fail")
    public String fail() {
        return myService.fail();
    }

	public static void main(String[] args) {
        SpringApplication.run(HystrixApplication.class, args);
    }
}
