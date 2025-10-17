package fr.campus.dungeoncrawlerapi.domain.characters.players;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.campus.dungeoncrawlerapi.domain.characters.Character;
import fr.campus.dungeoncrawlerapi.domain.equipments.defensive.DefensiveEquipment;
import fr.campus.dungeoncrawlerapi.domain.equipments.offensive.OffensiveEquipment;

import fr.campus.dungeoncrawlerapi.domain.game.Game;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "player")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class Player extends Character {

    @ManyToOne
    @JoinColumn(name = "offensive_equipment_id")
    protected OffensiveEquipment offensiveEquipment;

    @ManyToOne
    @JoinColumn(name = "defensive_equipment_armor_id")
    protected DefensiveEquipment defensiveEquipmentArmor;

    @ManyToOne
    @JoinColumn(name = "defensive_equipment_helmet_id")
    protected DefensiveEquipment defensiveEquipmentHelmet;

    @ManyToOne
    @JoinColumn(name = "defensive_equipment_shield_id")
    protected DefensiveEquipment defensiveEquipmentShield;

    @OneToMany(mappedBy = "player")
    @JsonIgnore
    private List<Game> games;

    @Transient
    private boolean isFleeing = false;
    @Transient
    private boolean isDead = false;

    protected int health;
    private int experience;
    private int level;
    private int experienceToNextLevel;

    public Player() {
        this.experience = 0;
        this.level = 1;
        this.experienceToNextLevel = 100;
    }

    protected Player(String name) {
        super(name);
    }

    protected Player(int id, String name, int health, int maxHealth, int base_attack) {
        super(id, name, maxHealth, base_attack);
        this.health = health;
    }

    protected Player(int id, String name, int health, int maxHealth, int base_attack, OffensiveEquipment offensiveEquipment) {
        super(id, name, maxHealth, base_attack);
        this.health = health;
        this.offensiveEquipment = offensiveEquipment;
    }

    public int getTotalAttack() {
        return base_attack + (offensiveEquipment != null ? offensiveEquipment.getAttack() : 0);
    }

    public int getTotalDefense() {
        int defense = base_defense;
        if (defensiveEquipmentArmor != null) defense += defensiveEquipmentArmor.getDefense();
        if (defensiveEquipmentHelmet != null) defense += defensiveEquipmentHelmet.getDefense();
        if (defensiveEquipmentShield != null) defense += defensiveEquipmentShield.getDefense();
        return defense;
    }

    public void gainExperience(int exp) {
        this.experience += exp;
        checkLevelUp();
    }

    private void checkLevelUp() {
        if (experience >= experienceToNextLevel) {
            levelUp();
        }
    }

    private void levelUp() {
        level++;
        experience -= experienceToNextLevel;
        experienceToNextLevel = level * 100; // Plus d'expérience nécessaire à chaque niveau

        // Amélioration des stats
        maxHealth += 2;
        health = maxHealth; // Soin complet lors du level up
        base_attack += 1;

        System.out.println("🎉 Niveau " + level + " ! Vos stats s'améliorent !");
        System.out.println("❤️ HP: " + health + "/" + maxHealth + " (+2) | ⚔️ ATK: " + base_attack + " (+1)");
    }

    public abstract String getType();
}
