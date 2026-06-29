package lab.coder.colly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CollyApplication {

    public static void main(String[] args) {
        SpringApplication.run(CollyApplication.class, args);
    }

}
