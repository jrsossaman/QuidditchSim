package com.example.quidditchsim.Position;

import com.example.quidditchsim.Player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PositionService {

    public PositionService(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    @Autowired
    private final PositionRepository positionRepository;

    public List<Position> getPositions() {
        return positionRepository.findAll();
    }

    public Position getOnePosition(Long positionId) {
        return positionRepository.findById(positionId).get();
                //.orElseThrow(() -> new IllegalStateException("Position not found"));
    }

    public void addNewPosition(Position position) {
        Optional<Position> positionOptional = positionRepository.findPositionByName(position.getName());
        if (positionOptional.isPresent()) {
            throw new IllegalStateException("Name taken.");
        }
        positionRepository.save(position);
    }

    public void updatePositionName(Long positionId, String name) {
        Position position = positionRepository.findById(positionId).orElseThrow(() -> new IllegalStateException(
                "Position with id " + positionId + " doesn't exist."));
        if (name != null &&
                name.length() > 0 &&
                !Objects.equals(position.getName(), name)) {
            position.setName(name);
            positionRepository.save(position);
        }
    }

    public void updatePositionDescription(Long positionId, String description) {
        System.out.println("Description: " + description);
        Position position = positionRepository.findById(positionId).orElseThrow(() -> new IllegalStateException(
                "Position with id " + positionId + " doesn't exist."));
        if (description != null &&
                description.length() > 0 &&
                !Objects.equals(position.getDescription(), description)) {
            position.setDescription(description);
            positionRepository.save(position);
        }
    }

    public void deletePosition(Long positionId) {
        boolean exists = positionRepository.existsById(positionId);
        if (!exists) {
            throw new IllegalStateException("Position with id " + positionId + " doesn't exist.");
        }
        positionRepository.deleteById(positionId);
    }
}
