package com.forex.backend.wallet_management_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class WalletManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletManagementServiceApplication.class, args);
	}

}
