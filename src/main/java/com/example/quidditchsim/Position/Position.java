package com.example.quidditchsim.Position;

import com.example.quidditchsim.Player.Player;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table
public class Position {
    @Id
    @SequenceGenerator(
            name = "position_sequence",
            sequenceName = "position_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "position_sequence"
    )
    private Long id;
    private String name;
    private String description;

//    @ManyToMany
//    @JoinTable(
//            name = "player_assigned",
//            joinColumns = @JoinColumn(name = "position_id"),
//            inverseJoinColumns = @JoinColumn(name = "player_id")
//    )
//    private Set<Player> playerSet = new HashSet<>();

    @ManyToMany(mappedBy = "positionSet")
    private Set<Player> playerSet;

    public Position() {
    }

    public Position(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Position(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Player> getPlayerSet() {
        return playerSet;
    }

    public void assignPlayer(Player player) {
        this.playerSet.add(player);
        player.getPositionSet().add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Position that = (Position) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }

    @Override
    public String toString() {
        return "ID: " + id + "Name: " + name + " | Description: " + description;
    }
}
