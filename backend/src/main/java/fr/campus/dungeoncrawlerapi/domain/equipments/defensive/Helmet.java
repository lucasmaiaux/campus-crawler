package fr.campus.dungeoncrawlerapi.domain.equipments.defensive;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Helmet")
public class Helmet extends DefensiveEquipment {

    public Helmet() {
    }

    public Helmet(String name, int defense) {
        super(name, defense);
    }

    public String getType() {
        return "Helmet";
    }
}