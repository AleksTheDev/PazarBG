package bg.pazar.pazarbg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PazarBgApplication {

    public static void main(String[] args) {
        SpringApplication.run(PazarBgApplication.class, args);
    }

}
