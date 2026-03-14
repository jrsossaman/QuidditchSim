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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

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
                System.out.println("0. Exit");

                int command = input.nextInt();

                switch (command) {
                    case 1:
                        playerMenu();
                        break;
                    case 2:
                        positionMenu();
                        break;
                    case 0:
                        System.exit(0);
                    default:
                        System.out.println("Invalid selection. Try again.");
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
            System.out.println("3. Search Players");
            System.out.println("4. Delete Player");
            System.out.println("0. Back to Database Menu");

            int command = input.nextInt();
            input.nextLine();

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
                default:
                    System.out.println("Invalid selection. Try again.");
                    break;
            }
        }
    }

    // ----- case 1 - Create
    private void createNewPlayer() {
        System.out.println("\n======= CREATE NEW PLAYER =======");

        System.out.println("Player Name (Firstname Lastname):");
        String name = input.nextLine();

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

        Player newPlayer = new Player(name);
        newPlayer.assignHouse(house);
//        for (Position pos : positions) {
//            newPlayer.addPosition(pos);  // updates both player.positionSet AND position.playerSet
//        }
        newPlayer.getPositionSet().addAll(positions);
        playerService.addNewPlayer(newPlayer);

        System.out.println("Player " + name + " created successfully!");
    }

    //----- case 2 - UPDATE
    private void updatePlayer() {
        System.out.println("\n======= UPDATE PLAYER =======");
        viewAllPlayers();
        System.out.println("Enter Player ID:");
        Long id = input.nextLong();
        input.nextLine();

        try {
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
                default:
                    System.out.println("Invalid selection. Try again.");
            }

        } catch (IllegalStateException e) {
            System.out.println("That ID doesn't exist. Try again.");
            return;
        }
    }


    //----- case 3 - SEARCH PLAYER MENU
    private void searchPlayerMenu() {
        System.out.println("\n======= SEARCH PLAYER MENU =======");
        System.out.println("1. View All Players");
        System.out.println("2. Search Player By Name");
        System.out.println("0. Back to Player Actions");

        int command = input.nextInt();

        switch (command) {
            case 1:
                viewAllPlayers();
                break;
            case 2:
                playerByName();
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid selection. Try again.");
                break;
        }
    }

    private void viewAllPlayers() {
        System.out.println("\n======= ALL PLAYERS =======");
        List<Player> players = playerService.getPlayers();
        if (players == null || players.isEmpty()) {
            System.out.println("No players found.");
        }
        for (Player p : players) {
            System.out.println("ID: " + p.getId()
                + " | Name: " + p.getName()
                + " | Positions: " + p.getPositionSet()
                + " | House: " + p.getHouse()
            );
        }
    }

    private void playerByName() {
        input.nextLine();
        System.out.println("\n======= PLAYER BY NAME =======");
        System.out.println("Enter name:");
        String name = input.nextLine();
        List <Player> players = playerService.getPlayers();
        boolean found = false;
        for (Player p : players) {
            if (p.getName().equalsIgnoreCase(name)) {
                found = true;
                System.out.println("ID: " + p.getId()
                        + " | Name: " + p.getName()
                        + " | Positions: " + p.getPositionSet()
                        + " | House: " + p.getHouse()
                );
            }
        }
        if (!found) {
            System.out.println("No players found with that name.");
        }
    }

    //----- case 4 - DELETE
    private void deletePlayerMenu() {
        System.out.println("\n======= DELETE PLAYER =======");
        viewAllPlayers();
        System.out.println("Enter player ID or 0 to cancel:");
        Long id = input.nextLong();
        if (id == 0) return;
        input.nextLine();
        try {
            Player p = playerService.getOnePlayer(id);
            System.out.println(p.getName() + " selected.");
            System.out.println("Are you sure you wish to delete this player?");
            System.out.println("THIS ACTION IS PERMANENT AND CANNOT BE UNDONE!");
            System.out.println("Enter y/n");
            String answer = input.nextLine();
            if (answer.equals("y")) {
                playerService.deletePlayer(id);
            }
        } catch (IllegalStateException e) {
            System.out.println("That ID doesn't exist. Try again.");
            return;
        }
    }


    // ####### POSITION ACTIONS #######
    private void positionMenu() {
        System.out.println("\n======= POSITION ACTIONS =======");
        System.out.println("1. Create New Position");
        System.out.println("2. Search Positions");
        System.out.println("3. Update Positions");
        System.out.println("4. Delete Position");
        System.out.println("0. Back to Database Menu");

        int command = input.nextInt();
        input.nextLine();

        switch (command) {
            case 1:
                createNewPosition();
                break;
            case 2:
                searchPositionMenu();
                break;
            case 3:
                updatePosition();
                break;
            case 4:
                deletePosition();
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid selection. Try again.");
                break;
        }
    }

    // ------- case 1: CREATE
    private void createNewPosition() {
        System.out.println("\n======= CREATE POSITION =======");
        System.out.println("Name: ");
        String name = input.nextLine();
        System.out.println("Description: ");
        String description = input.nextLine();

        Position newPosition = new Position(
                name, description);

        positionService.addNewPosition(newPosition);

        System.out.println("Player " + name + " created successfully!");
    }

    // ----- case 2: SEARCH
    private void searchPositionMenu() {
        System.out.println("\n======= SEARCH POSITIONS MENU =======");
        System.out.println("1. View All Positions");
        System.out.println("2. Search By Name");
        System.out.println("0. Back to Position Actions Menu");

        int command = input.nextInt();

        switch (command) {
            case 1:
                viewAllPositions();
                break;
            case 2:
                positionByName();
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid selection. Try again.");
                break;
        }
    }

    private void viewAllPositions() {
        System.out.println("\n======= ALL POSITIONS =======");
        List<Position> positions = positionService.getPositions();
        if (positions == null || positions.isEmpty()) {
            System.out.println("No positions found.");
        }
        for (Position p : positions) {
            System.out.println("ID: " + p.getId()
                    + " | Name: " + p.getName()
                    + " | Description: " + p.getDescription()
            );
        }
    }

    private void positionByName() {
        input.nextLine();
        System.out.println("\n======= POSITION BY NAME =======");
        System.out.println("Enter name:");
        String name = input.nextLine();
        List <Position> positions = positionService.getPositions();
        boolean found = false;
        for (Position p : positions) {
            if (p.getName().equalsIgnoreCase(name)) {
                found = true;
                System.out.println("ID: " + p.getId()
                        + " | Name: " + p.getName()
                        + " | Description: " + p.getDescription()
                );
            }
        }
        if (!found) {
            System.out.println("No positions found with that name.");
        }
    }

    // ----- case 3: UPDATE
    private void updatePosition() {
        System.out.println("\n======= UPDATE POSITION =======");
        viewAllPositions();
        System.out.println("Enter position ID:");
        Long id = input.nextLong();
        input.nextLine();

        try {
            Position position = positionService.getOnePosition(id);
            System.out.println("\n--- UPDATE: " + position.getName() + " ---");
            System.out.println("1. Name");
            System.out.println("2. Description");
            System.out.println("0. Cancel");

            int command = input.nextInt();

            switch (command) {
                case 1:
                    input.nextLine();
                    System.out.println("Enter new name:");
                    String newName = input.nextLine();
                    positionService.updatePositionName(id, newName);
                    System.out.println("Name updated.");
                    break;
                case 2:
                    input.nextLine();
                    System.out.println("Enter new description:");
                    String newDescription = input.nextLine();
                    positionService.updatePositionDescription(id, newDescription);
                    System.out.println("Description updated successfully.");
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid selection. Try again.");
            }
        } catch (NoSuchElementException e) {
            System.out.println("ID doesn't exist. Try again.");
            return;
        }
    }

    private void deletePosition() {
        System.out.println("\n======= DELETE POSITION =======");
        viewAllPositions();
        System.out.println("Enter position ID:");
        Long id = input.nextLong();
        input.nextLine();
        try {
            Position p = positionService.getOnePosition(id);
            if (p == null) {
                System.out.println("Position not found.");
                return;
            }
            System.out.println(p.getName() + " selected.");
            System.out.println("Are you sure you wish to delete this position?");
            System.out.println("THIS ACTION IS PERMANENT AND CANNOT BE UNDONE!");
            System.out.println("Enter y/n");
            String answer = input.nextLine();
            if (answer.equals("y")) {
                try {
                    positionService.deletePosition(id);
                    System.out.println("Position deleted successfully.");
                } catch (DataIntegrityViolationException e) {
                    System.out.println("You cannot delete this position. Players are currently assigned to it.");
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("ID doesn't exist. Try again.");
            return;
        }
    }

}
