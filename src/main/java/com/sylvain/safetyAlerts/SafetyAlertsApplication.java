package com.sylvain.safetyAlerts;

import com.sylvain.safetyAlerts.repository.DataRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class SafetyAlertsApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(SafetyAlertsApplication.class, args);
		DataRepository  dataRepository = new DataRepository();
		System.out.println(dataRepository.getAllPersons().size());
	}

}
