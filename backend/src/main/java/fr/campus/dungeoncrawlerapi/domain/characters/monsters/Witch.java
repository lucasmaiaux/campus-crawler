package fr.campus.dungeoncrawlerapi.domain.characters.monsters;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Witch")
public class Witch extends Monster {

    public Witch() {
    }

    public Witch(String name) {
        super(name);
        this.maxHealth = 20;
        this.base_attack = 10;
    }

    public Witch(int id, String name, int maxHealth, int base_attack) {
        super(id, name, maxHealth, base_attack);
    }

    public String getType() {
        return "Witch";
    }

}
