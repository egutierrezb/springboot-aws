package com.booking.room;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
public class RoomApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoomApplication.class, args);
	}

}
