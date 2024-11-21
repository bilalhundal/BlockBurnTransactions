package com.bilal.hundal1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BlockBurnTransactionsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlockBurnTransactionsApplication.class, args);
	}

}
