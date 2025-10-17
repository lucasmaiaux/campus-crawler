package fr.campus.dungeoncrawlerapi.domain.characters.players;

import fr.campus.dungeoncrawlerapi.domain.equipments.defensive.DefensiveEquipment;
import fr.campus.dungeoncrawlerapi.domain.equipments.defensive.Shield;
import fr.campus.dungeoncrawlerapi.domain.equipments.offensive.OffensiveEquipment;
import fr.campus.dungeoncrawlerapi.domain.equipments.offensive.Weapon;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Warrior")
public class Warrior extends Player {

    public Warrior() {
        super();
    }

    public Warrior(String name) {
        super(name);
        this.health = 10;
        this.maxHealth = 10;
        this.base_attack = 10;
        this.base_defense = 0;
        this.offensiveEquipment = new Weapon("Sword", 1);
        this.defensiveEquipmentHelmet = new Shield("Wooden Shield", 2);
    }

    public Warrior(int id, String name, int health, int maxHealth, int base_attack) {
        super(id, name, health, maxHealth, base_attack);
        this.offensiveEquipment = new Weapon("Sword", 1);
        this.defensiveEquipmentHelmet = new Shield("Wooden Shield", 2);
    }

    public Warrior(int id, String name, int health, int maxHealth, int base_attack, OffensiveEquipment offensiveEquipment) {
        super(id, name, health, maxHealth, base_attack, offensiveEquipment);
    }

    public String getType() {
        return "Warrior";
    }

}
