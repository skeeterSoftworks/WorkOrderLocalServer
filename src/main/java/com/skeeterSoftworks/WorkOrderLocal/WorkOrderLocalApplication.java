package com.skeeterSoftworks.WorkOrderLocal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WorkOrderLocalApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkOrderLocalApplication.class, args);
	}

}
