package com.wxxzin.portfolio.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import io.github.cdimascio.dotenv.Dotenv;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableScheduling
@EnableJpaAuditing
@EnableTransactionManagement
@EnableAsync
@EnableRetry
@SpringBootApplication
public class PortfolioServerApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.configure().load();
		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

		SpringApplication.run(PortfolioServerApplication.class, args);
	}

}
