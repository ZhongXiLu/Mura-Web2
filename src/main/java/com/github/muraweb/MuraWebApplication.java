package com.github.muraweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MuraWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(MuraWebApplication.class, args);
	}

}
