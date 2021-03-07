package com.openclassrooms.safetynetalerts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.openclassrooms.safetynetalerts.service.LoggerApi;

//@ComponentScan(basePackages = { "com.openclassrooms.safetynetalerts.dao" })
@SpringBootApplication
public class ApiSafetyNetAlertsApplication {

	public static void main(String[] args) {

		SpringApplication.run(ApiSafetyNetAlertsApplication.class, args);

		// configuration the log for the API. For the test we have another definition
		// key
		// this definition key configure the type of the messages in LoggerApi class
		setLogger();

	}

	public static void setLogger() {
		LoggerApi.setLogger();
	}
}
