package com.example.quidditchsim.Player;

import com.example.quidditchsim.House.House;
import com.example.quidditchsim.Position.Position;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table
public class Player {
    @Id
    @SequenceGenerator(
            name = "player_sequence",
            sequenceName = "player_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "player_sequence"
    )
    private Long id;
    private String name;

//    @ManyToMany(mappedBy = "playerSet", fetch = FetchType.EAGER)
//    private Set<Position> positionSet = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "player_assigned",
            joinColumns = @JoinColumn(name="player_id"),
            inverseJoinColumns = @JoinColumn(name="position_id"))
    private Set<Position> positionSet = new HashSet<>();

    @ManyToOne//(cascade = CascadeType.ALL)
    @JoinColumn(name = "house_id", referencedColumnName = "id")
    private House house;

    public Player() {
    }

    public Player(String name) {
        this.name = name;
    }

    public Player(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Player(String name, Integer maxHp, Integer curHp, Integer ac, Integer proBonus, Integer strBonus, Integer dexBonus, Integer conBonus, Integer wisBonus, boolean acrProf, boolean athProf, boolean perProf, boolean brmProf, boolean qflProf) {
        this.name = name;
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

    public House getHouse() {
        return house;
    }

    public void assignHouse(House house) {
        this.house = house;
    }

    //trying to get positions to not be empty
//    public void addPosition(Position position) {
//        this.positionSet.add(position);          // update inverse side
//        position.getPlayerSet().add(this);       // update owning side
//    }

    public Set<Position> getPositionSet() {
        return positionSet;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(id, player.id) && Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}
