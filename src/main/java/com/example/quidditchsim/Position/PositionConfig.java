//package com.example.quidditchsim.Position;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.List;
//
//@Configuration
//public class PositionConfig {
//
//    @Bean
//    CommandLineRunner positionLineRunner(PositionRepository repository) {
//        return args -> {
//            Position chaser = new Position(
//                    "Chaser",
//                    "Attacking position. Drives the quaffle to the goal posts and attempts to score."
//            );
//
//            Position beater = new Position(
//                    "Beater",
//                    "Defense position. Bats bludgers at opposition to force turnover and create advantage."
//            );
//
//            repository.saveAll(List.of(chaser, beater));
//        };
//    };
//}
