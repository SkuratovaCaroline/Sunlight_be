package com.sunlight_cinema.Sunlight_cinema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SunlightCinemaApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SunlightCinemaApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(SunlightCinemaApplication.class, args);
    }
}
