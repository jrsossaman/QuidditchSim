package com.example.quidditchsim;

import com.example.quidditchsim.House.House;
import com.example.quidditchsim.House.HouseService;
import com.example.quidditchsim.Player.Player;
import com.example.quidditchsim.Player.PlayerService;
import com.example.quidditchsim.Position.Position;
import com.example.quidditchsim.Position.PositionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

@SpringBootApplication
@RestController
public class QuidditchSimApplication {
    private static HouseService houseService;
    private static PlayerService playerService;
    private static PositionService positionService;
    private Scanner input = new Scanner(System.in);

    public QuidditchSimApplication(
            HouseService houseService,
            PlayerService playerService,
            PositionService positionService
    ) {
        this.houseService = houseService;
        this.playerService = playerService;
        this.positionService = positionService;
    }

    public static void main(String[] args) {
        SpringApplication.run(QuidditchSimApplication.class, args);
    }

    @Bean
    CommandLineRunner database_actions() {
        return args -> {
            while (true) {
                System.out.println("\n======= DATABASE MENU =======");
                System.out.println("1. Player Actions");
                System.out.println("2. Position Actions");
                System.out.println("3. House Actions");
                System.out.println("0. Exit");

                int command = input.nextInt();

                switch (command) {
                    case 1:
                        playerMenu();
                        break;
                    case 2:
                        positionMenu();
                        break;
                    case 3:
                        house_menu();
                        break;
                    case 0:
                        System.exit(0);
                }
            }
        };
    }


//  ####### PLAYER ACTIONS  #######
    private void playerMenu() {
        while(true) {
            System.out.println("\n======= PLAYER ACTIONS =======");
            System.out.println("1. Create New Player");
            System.out.println("2. Update Existing Player");
            System.out.println("3. View All Players");
            System.out.println("4. Search Player");
            System.out.println("0. Back to Database Menu");

            int command = input.nextInt();

            switch (command) {
                case 1:
                    createNewPlayer();
                    break;
                case 2:
                    updatePlayer();
                    break;
                case 3:
                    searchPlayerMenu();
                    break;
                case 4:
                    deletePlayerMenu();
                    break;
                case 0:
                    return;
            }
        }
    }

    // ----- case 1 - Create
    private void createNewPlayer() {
        System.out.println("\n======= CREATE NEW PLAYER =======");

        System.out.println("Player Name (Firstname Lastname):");
        String name = input.nextLine();

        System.out.println("Maximum Hit Points:"); // USE CONBONUS BELOW TO CALC.
        int maxHp = input.nextInt();
        int curHp = maxHp;

        System.out.println("Proficiency Bonus:");
        int proBonus = input.nextInt();

        System.out.println("Strength Bonus:");
        int strBonus = input.nextInt();

        System.out.println("Dexterity Bonus:");
        int dexBonus = input.nextInt();
        int ac = 11 + dexBonus;

        System.out.println("Constitution Bonus:");
        int conBonus = input.nextInt();

        System.out.println("Wisdom Bonus:");
        int wisBonus = input.nextInt();

        System.out.println("Acrobatics proficiency? (y/n)");
        boolean acrProf = input.next().equalsIgnoreCase("y");

        System.out.println("Athletics proficiency? (y/n)");
        boolean athProf = input.next().equalsIgnoreCase("y");

        System.out.println("Perception proficiency? (y/n)");
        boolean perProf = input.next().equalsIgnoreCase("y");

        System.out.println("Broomstick proficiency? (y/n)");
        boolean brmProf = input.next().equalsIgnoreCase("y");

        System.out.println("Quaffle/Beater's Club Proficiency? (y/n)");
        boolean qflProf = input.next().equalsIgnoreCase("y");
        input.nextLine();

        System.out.println("Position Choices:");
        for (Position p : positionService.getPositions()) {
            System.out.println(p.getId() + " | " + p.getName());
        }
        Set<Position> positions = new HashSet<>();
        System.out.println("Enter IDs for starting positions, separated by ',':");
        System.out.println("(You can add more later.)");
        String posInput = input.nextLine();
        for (String idStr: posInput.split(",")) {
            Long posId = Long.parseLong(idStr.trim());
            Position pos = positionService.getOnePosition(posId);
            positions.add(pos);
        }

        System.out.println("House Choices:");
        for (House h : houseService.getHouses()) {
            System.out.println(h.getId() + " | " + h.getName());
        }
        System.out.println("Enter house ID:");
        Long houseId = input.nextLong();
        House house = houseService.getOneHouse(houseId);

        Player newPlayer = new Player(
                name, maxHp, curHp, ac, proBonus,
                strBonus, dexBonus, conBonus, wisBonus,
                acrProf, athProf, perProf, brmProf, qflProf);
        newPlayer.assignHouse(house);
        newPlayer.getPositionSet().addAll(positions);
        playerService.addNewPlayer(newPlayer);

        System.out.println("Player " + name + " created successfully!");
    }

    //----- case 2 - UPDATE
    private void updatePlayer() {
        while (true) {
            System.out.println("\n======= UPDATE PLAYER =======");
            System.out.println("Enter Player ID:");
            Long id = input.nextLong();
            Player player = playerService.getOnePlayer(id);
            System.out.println("\n--- UPDATE: " + player.getName() + " ---");
            System.out.println("1. Name");
            System.out.println("2. Positions");
            System.out.println("0. Cancel");

            int command = input.nextInt();

            switch (command) {
                case 1:
                    input.nextLine();
                    System.out.println("Enter new name:");
                    String newName = input.nextLine();
                    playerService.updatePlayer(id, newName);
                    System.out.println("Name updated.");
                    break;
                case 2:
                    System.out.println("Available Positions:");
                    for (Position p : positionService.getPositions()) {
                        System.out.println(p.getId() + ": " + p.getName());
                    }

                    input.nextLine();
                    System.out.println("Enter position IDs to add (comma separated):");
                    String posInput = input.nextLine();

                    Set<Position> positionsToAdd = new HashSet<>();
                    for (String posIdStr : posInput.split(",")) {
                        Long posId = Long.parseLong(posIdStr.trim());
                        Position pos = positionService.getOnePosition(posId);
                        positionsToAdd.add(pos);
                    }

                    playerService.updatePlayerPositions(id, positionsToAdd);

                    System.out.println("Player positions updated.");
                    break;
                case 0:
                    return;
            }
        }
    }

    //----- case 3 - SEARCH PLAYER MENU
    private void searchPlayerMenu() {
        while (true) {
            System.out.println("\n======= SEARCH PLAYER MENU =======");
            System.out.println("1. View All Players");
            System.out.println("2. View Players By Team");
            System.out.println("3. View Players By Position");
            System.out.println("4. Search Player By ID");
            System.out.println("5. Search Player By Name");
            System.out.println("0. Back to Player Actions");

            int command = input.nextInt();

            switch (command) {
                case 1:
                    viewAllPlayers();
                    break;
                case 2:
                    playersByTeam();
                    break;
                case 3:
                    playersByPosition();
                    break;
                case 4:
                    playerById();
                    break;
                case 5:
                    playerByName();
                case 0:
                    return;
            }
        }
    }

    //----- case 4 - DELETE
    private void deletePlayerMenu() {
        System.out.println("\n======= DELETE PLAYER MENU =======");
    }
}
