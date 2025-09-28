package com.sunlight_cinema.Sunlight_cinema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SunlightCinemaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SunlightCinemaApplication.class, args);
	}

}

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/health")
    public String health() {
        return "Sunlight Cinema API is running!";
    }
}
