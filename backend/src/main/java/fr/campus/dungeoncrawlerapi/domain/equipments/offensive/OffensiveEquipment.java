package fr.campus.dungeoncrawlerapi.domain.equipments.offensive;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.campus.dungeoncrawlerapi.domain.characters.players.Player;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "offensive_equipment")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class OffensiveEquipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    //protected String type;
    protected String name;
    protected int attack;

    @OneToMany(mappedBy = "offensiveEquipment")
    @JsonIgnore
    private List<Player> players;

    public OffensiveEquipment() {
    }

    public OffensiveEquipment(String name, int attack) {
        this.name = name;
        this.attack = attack;
    }

    @Override
    public String toString() {
        return name + " (" + attack + ")";
    }
}