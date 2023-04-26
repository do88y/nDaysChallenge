package challenge.nDaysChallenge;

import org.hibernate.annotations.Source;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
public class NDaysChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(NDaysChallengeApplication.class, args);
	}

}
