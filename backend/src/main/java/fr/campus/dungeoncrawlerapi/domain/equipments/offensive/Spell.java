package fr.campus.dungeoncrawlerapi.domain.equipments.offensive;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Spell")
public class Spell extends OffensiveEquipment {

    public Spell() {
    }

    public Spell(String name, int attack) {
        super(name, attack);
    }

    public String getType() {
        return "Spell";
    }
}
