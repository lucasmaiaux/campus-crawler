package fr.campus.dungeoncrawlerapi.domain.equipments.defensive;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Armor")
public class Armor extends DefensiveEquipment {

    public Armor() {
    }

    public Armor(String name, int defense) {
        super(name, defense);
    }

    public String getType() {
        return "Armor";
    }
}