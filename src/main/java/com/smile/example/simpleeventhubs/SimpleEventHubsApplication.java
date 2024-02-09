package com.smile.example.simpleeventhubs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class SimpleEventHubsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleEventHubsApplication.class, args);
	}

}
