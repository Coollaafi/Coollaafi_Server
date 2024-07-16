package coollaafi.wot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class WotApplication {

	public static void main(String[] args) {
		SpringApplication.run(WotApplication.class, args);
	}

}
