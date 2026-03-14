package com.example.quidditchsim.Player;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PlayerConfig {
    @Bean
    CommandLineRunner playerLineRunner(PlayerRepository repository) {
        return args -> {
          Player test = new Player(
                  "Test Player"
          );

          repository.saveAll(List.of(test));
        };
    }
}
