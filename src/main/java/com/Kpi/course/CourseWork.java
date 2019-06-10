package com.Kpi.course;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * The class to initialization start point of program with framework {@link SpringBootApplication}
 *
 * @version 1.0
 * @see SpringApplication
 */
@SpringBootApplication
public class CourseWork {
    private static ConfigurableApplicationContext context;

    /**
     * start server and initialize all needed bean and services for work
     *
     * @param args for program
     */
    public static void main(String[] args) {
        context = SpringApplication.run(CourseWork.class, args);
    }
}
