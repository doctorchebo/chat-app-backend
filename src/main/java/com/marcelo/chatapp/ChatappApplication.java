package com.marcelo.chatapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EntityScan("com.marcelo.chatapp.model")
@ComponentScan(basePackages = "com.marcelo.chatapp")
public class ChatappApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatappApplication.class, args);
	}

}
