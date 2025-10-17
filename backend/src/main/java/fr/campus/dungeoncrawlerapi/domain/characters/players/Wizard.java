package fr.campus.dungeoncrawlerapi.domain.characters.players;

import fr.campus.dungeoncrawlerapi.domain.equipments.defensive.Armor;
import fr.campus.dungeoncrawlerapi.domain.equipments.defensive.DefensiveEquipment;
import fr.campus.dungeoncrawlerapi.domain.equipments.offensive.OffensiveEquipment;
import fr.campus.dungeoncrawlerapi.domain.equipments.offensive.Spell;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Wizard")
public class Wizard extends Player {

    protected int health;

    public Wizard() {
        super();
    }

    public Wizard(String name) {
        super(name);
        this.health = 8;
        this.maxHealth = 8;
        this.base_attack = 12;
        this.base_defense = 0;
        this.offensiveEquipment = new Spell("Fireball", 1);
        this.defensiveEquipmentHelmet = new Armor("Robe de mage", 2);
    }

    public Wizard(int id, String name, int health, int maxHealth, int base_attack) {
        super(id, name, health, maxHealth, base_attack);
        this.offensiveEquipment = new Spell("Fireball", 1);
        this.defensiveEquipmentHelmet = new Armor("Robe de mage", 2);
    }

    public Wizard(int id, String name, int health, int maxHealth, int base_attack, OffensiveEquipment offensiveEquipment) {
        super(id, name, health, maxHealth, base_attack, offensiveEquipment);
    }

    public String getType() {
        return "Wizard";
    }
}
