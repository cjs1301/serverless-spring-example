package com.example.NoMoney;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class NoMoneyApplication {

	public static void main(String[] args) {
		SpringApplication.run(NoMoneyApplication.class, args);
	}

}
