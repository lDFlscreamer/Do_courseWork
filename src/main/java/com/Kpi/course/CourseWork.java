package com.Kpi.course;

import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The class to initialization start point of program with framework {@link SpringBootApplication}
 *
 * @version 1.0
 * @see SpringApplication
 */
@SpringBootApplication
@Configuration
public class CourseWork {
    @Getter
    private static ConfigurableApplicationContext context;

    /**
     * start server and initialize all needed bean and services for work
     *
     * @param args for program
     */
    public static void main(String[] args) {
        context = SpringApplication.run(CourseWork.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("*");
            }
        };
    }
}
