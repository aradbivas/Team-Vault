package com.bivas.teamvault;

import com.bivas.teamvault.email.EmailProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(EmailProperties.class)
@SpringBootApplication
public class TeamVaultApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeamVaultApplication.class, args);
	}

}
