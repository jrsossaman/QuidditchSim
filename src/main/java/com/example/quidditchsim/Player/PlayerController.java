package com.example.quidditchsim.Player;

import com.example.quidditchsim.House.House;
import com.example.quidditchsim.House.HouseService;
import com.example.quidditchsim.Position.PositionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/players")
public class PlayerController {

    private final PlayerService playerService;
    private final HouseService houseService;

    @Autowired
    public PlayerController(PlayerService playerService, HouseService houseService) {
        this.playerService = playerService;
        this.houseService = houseService;
    }

    @Autowired
    public PlayerRepository playerRepository;

    @GetMapping
    public List<Player> getPlayers() {
        return playerService.getPlayers();
    }

    @PostMapping
    public void addPlayer(@RequestBody Player player) {
        playerService.addNewPlayer(player);
    }

    @DeleteMapping(path = "{playerId}")
    public void deleteStudent(@PathVariable("playerId") Long playerId) {
        playerService.deletePlayer(playerId);
    }

    @PutMapping(path = "{playerId}")
    public void updatePlayer(
            @PathVariable("playerId") Long playerId,
            @RequestParam(required = false) String name) {
        playerService.updatePlayer(playerId, name);
    }

    @PutMapping(path = "/{playerId}/house-teams/{houseId}")
    Player assignHouseToPlayer(
            @PathVariable Long playerId,
            @PathVariable Long houseId
    ) {
        Player player = playerService.getOnePlayer(playerId);
        House house = houseService.getOneHouse(houseId);
        player.assignHouse(house);
        return playerRepository.save(player);
    }

}
