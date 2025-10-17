package fr.campus.dungeoncrawlerapi.domain.characters.monsters;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Dragon")
public class Dragon extends Monster {

    public Dragon() {
    }

    public Dragon(String name) {
        super(name);
        this.maxHealth = 25;
        this.base_attack = 12;
    }

    public Dragon(int id, String name, int maxHealth, int attack) {
        super(id, name, maxHealth, attack);
    }

    public String getType() {
        return "Dragon";
    }

}
