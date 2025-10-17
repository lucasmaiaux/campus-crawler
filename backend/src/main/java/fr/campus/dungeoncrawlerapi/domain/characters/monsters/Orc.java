package fr.campus.dungeoncrawlerapi.domain.characters.monsters;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Orc")
public class Orc extends Monster {

    public Orc() {
    }

    public Orc(String name) {
        super(name);
        this.maxHealth = 18;
        this.base_attack = 9;
    }

    public Orc(int id, String name, int maxHealth, int attack) {
        super(id, name, maxHealth, attack);
    }

    public String getType() {
        return "Orc";
    }

}