package fr.campus.dungeoncrawlerapi.domain.characters.monsters;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Goblin")
public class Goblin extends Monster {

    public Goblin() {
    }

    public Goblin(String name) {
        super(name);
        this.maxHealth = 12;
        this.base_attack = 7;
    }

    public Goblin(int id, String name, int maxHealth, int attack) {
        super(id, name, maxHealth, attack);
    }

    public String getType() {
        return "Goblin";
    }

}
