package com.fabiankaraben.stocksimulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StockPriceSimulatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockPriceSimulatorApplication.class, args);
	}

}
