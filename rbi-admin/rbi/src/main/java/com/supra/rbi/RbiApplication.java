package com.supra.rbi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@ComponentScan(basePackages = "com.supra.rbi")
public class RbiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RbiApplication.class, args);
	}

}
