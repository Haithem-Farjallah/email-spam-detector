package com.medianet.spamDetection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling

public class SpamDetectionApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpamDetectionApplication.class, args);
	}

}
