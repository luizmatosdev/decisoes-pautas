package com.luiz.decisoespautas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class DecisoesPautasApplication {

    public static void main(String[] args) {
        SpringApplication.run(DecisoesPautasApplication.class, args);
    }

}
