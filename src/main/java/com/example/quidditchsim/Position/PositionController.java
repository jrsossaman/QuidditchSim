package com.example.quidditchsim.Position;

import com.example.quidditchsim.Player.Player;
import com.example.quidditchsim.Player.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/positions")
public class PositionController {

    private final PositionService positionService;
    private final PlayerService playerService;

    @Autowired
    public PositionController(PositionService positionService, PlayerService playerService) {
        this.positionService = positionService;
        this.playerService = playerService;
    }

    @Autowired
    public PositionRepository positionRepository;

    @GetMapping
    public List<Position> getPositions() {
        return positionService.getPositions();
    }

    @PutMapping("/{positionId}/players/{playerId}")
    Position putPlayerAtPostion(
            @PathVariable Long positionId,
            @PathVariable Long playerId
    ) {
        Position position = positionService.getOnePosition(positionId);
        Player player = playerService.getOnePlayer(playerId);
        position.assignPlayer(player);
        return positionRepository.save(position);
    }

}
