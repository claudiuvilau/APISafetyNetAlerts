package com.openclassrooms.safetynetalerts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@ComponentScan(basePackages = { "com.openclassrooms.safetynetalerts.dao" })
@SpringBootApplication
public class ApiSafetyNetAlertsApplication {

	public static void main(String[] args) {

		SpringApplication.run(ApiSafetyNetAlertsApplication.class, args);

		// configuration the log for the API. For the test we have another definition
		// key
		// this definition key configure the type of the messages in LoggerApi class
		System.setProperty("log4j.configurationFile", "log4j2.xml");

	}
}
