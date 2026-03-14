package com.example.quidditchsim.Player;

import com.example.quidditchsim.House.House;
import com.example.quidditchsim.House.HouseRepository;
import com.example.quidditchsim.Position.Position;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class PlayerService {

    @Autowired
    HouseRepository houseRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Autowired
    private final PlayerRepository playerRepository;

//    @Transactional
//    public List<Player> getPlayers() {
//        return playerRepository.findAll();
//    }

    @Transactional
    public List<Player> getPlayers() {
        List<Player> players = playerRepository.findAll();
        // Force initialization of lazy collections
        players.forEach(p -> p.getPositionSet().size());
        return players;
    }

    public Player getOnePlayer(Long playerId) {
        return playerRepository.findById(playerId)//.get();
            .orElseThrow(() -> new IllegalStateException("Player not found."));
    }

    public void addNewPlayer(Player player) {
        Optional<Player> playerOptional = playerRepository.findPlayerByName(player.getName());
        if (playerOptional.isPresent()) {
            throw new IllegalStateException("Name taken.");
        }
        Long houseId = player.getHouse().getId();
        House managedHouse = houseRepository.findById(houseId)
                .orElseThrow(() -> new RuntimeException("House not found"));

        player.assignHouse(managedHouse);
        playerRepository.save(player);
    }

    public void deletePlayer(Long playerId) {
        boolean exists = playerRepository.existsById(playerId);
        if (!exists) {
            throw new IllegalStateException("Player with id " + playerId + " doesn't exist.");
        }
        playerRepository.deleteById(playerId);
    }

    @Transactional
    public void updatePlayer(Long playerId, String name) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new IllegalStateException(
                "Player with id " + playerId + "doesn't exist."));
        if (name != null &&
                name.length() > 0 &&
                !Objects.equals(player.getName(), name)) {
            player.setName(name);
        }
    }

    @Transactional
    public void updatePlayerPositions(Long playerId, Set<Position> newPositions) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalStateException(
                        "Player with id " + playerId + " doesn't exist."));
        player.getPositionSet().addAll(newPositions);
    }

}
