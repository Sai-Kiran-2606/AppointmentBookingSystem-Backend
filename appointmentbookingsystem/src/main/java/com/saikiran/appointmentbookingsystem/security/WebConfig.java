package com.saikiran.appointmentbookingsystem.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // allow CORS on all endpoints
                .allowedOrigins("https://appointment-booking-system-frontend.vercel.app/") // your React dev server
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*") // or specify "Content-Type", "Authorization", etc.
                .exposedHeaders("Authorization") // if you need to read the Authorization header
                .allowCredentials(true); // if you need cookies or HTTP auth
    }
}
