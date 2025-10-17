package fr.campus.dungeoncrawlerapi.domain.equipments.defensive;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.campus.dungeoncrawlerapi.domain.characters.players.Player;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "defensive_equipment")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class DefensiveEquipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    //protected String type;
    protected String name;
    protected int defense;

    @OneToMany(mappedBy = "defensiveEquipmentHelmet")
    @JsonIgnore
    private List<Player> players;

    public DefensiveEquipment() {
    }

    public DefensiveEquipment(String name, int defense) {
        this.name = name;
        this.defense = defense;
    }

    public abstract String getType();

    @Override
    public String toString() {
        return name + " (" + defense + ")";
    }
}
