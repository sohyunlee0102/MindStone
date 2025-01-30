package Spring.MindStone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MindStoneApplication {

	public static void main(String[] args) {
		SpringApplication.run(MindStoneApplication.class, args);
	}

}
