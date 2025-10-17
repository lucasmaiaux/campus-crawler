package fr.campus.dungeoncrawlerapi.domain.equipments.offensive;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Weapon")
public class Weapon extends OffensiveEquipment {

    public Weapon() {
    }

    public Weapon(String name, int attack) {
        super(name, attack);
    }

    public String getType() {
        return "Weapon";
    }
}