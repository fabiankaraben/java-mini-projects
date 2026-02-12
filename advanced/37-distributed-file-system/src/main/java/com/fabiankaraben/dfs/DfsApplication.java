package com.fabiankaraben.dfs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DfsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DfsApplication.class, args);
	}

}
