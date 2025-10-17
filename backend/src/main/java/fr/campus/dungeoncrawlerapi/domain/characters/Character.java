package fr.campus.dungeoncrawlerapi.domain.characters;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    protected String name;
    protected int maxHealth;
    protected int base_attack;
    protected int base_defense;

    public Character() {
    }

    public Character(String name) {
        this.name = name;
    }

    public Character(int id, String name, int maxHealth, int base_attack) {
        this.id = id;
        this.name = name;
        this.maxHealth = maxHealth;
        this.base_attack = base_attack;
        this.base_defense = 0;
    }

    public abstract String getType();

    @Override
    public String toString() {
        return "Personnage : " +
                name + " (" +
                this.getType() + ")" +
                ", HP : " + maxHealth +
                ", Base Attack : " + base_attack +
                ", Base Defense : " + base_defense;
    }

}