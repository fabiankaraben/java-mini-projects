package com.example.transcoding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TranscodingApplication {

	public static void main(String[] args) {
		SpringApplication.run(TranscodingApplication.class, args);
	}

}
