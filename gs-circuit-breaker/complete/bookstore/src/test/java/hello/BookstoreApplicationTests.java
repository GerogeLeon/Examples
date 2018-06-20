package hello;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookstoreApplicationTests {

    @Autowired
    private TestRestTemplate rest;

    @Test
    public void recommendedTest() {
        String resp = rest.getForObject("/recommended", String.class);
        assertThat(resp).isEqualTo("Spring in Action (Manning), Cloud Native Java (O'Reilly), Learning Spring Boot (Packt)");
    }
}
