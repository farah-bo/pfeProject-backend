package com.example.segulaproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication (exclude = {SecurityAutoConfiguration.class})
public class SEGULAProject {

    public static void main(String[] args) {
        SpringApplication.run(SEGULAProject.class, args);
    }

}
