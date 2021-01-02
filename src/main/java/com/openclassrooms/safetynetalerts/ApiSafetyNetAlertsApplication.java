package com.openclassrooms.safetynetalerts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@ComponentScan(basePackages = { "com.openclassrooms.safetynetalerts.dao" })
@SpringBootApplication
public class ApiSafetyNetAlertsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiSafetyNetAlertsApplication.class, args);

	}

}
