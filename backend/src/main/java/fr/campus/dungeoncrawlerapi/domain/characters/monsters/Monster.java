package fr.campus.dungeoncrawlerapi.domain.characters.monsters;

import fr.campus.dungeoncrawlerapi.domain.characters.Character;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "monster")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class Monster extends Character {

    public Monster() {
    }

    protected Monster(String name) {
        super(name);
    }

    protected Monster(int id, String name, int maxHealth, int base_attack) {
        super(id, name, maxHealth, base_attack);
    }

    @Override
    public String toString() {
        return "Monster{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", maxHealth=" + maxHealth +
                ", attack=" + base_attack +
                ", defense=" + base_defense +
                '}';
    }

    public abstract String getType();
}
