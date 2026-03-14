package com.example.quidditchsim.House;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class HouseConfig {
    @Bean
    CommandLineRunner houseLineRunner(HouseRepository repository) {
        return args -> {
            House gryffindor = new House(
                    "Gryffindor"
            );

            House hufflepuff = new House(
                    "Hufflepuff"
            );

            repository.saveAll(List.of(gryffindor, hufflepuff));

        };
    }
}
