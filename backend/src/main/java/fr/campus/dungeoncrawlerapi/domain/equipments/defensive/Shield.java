package fr.campus.dungeoncrawlerapi.domain.equipments.defensive;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Shield")
public class Shield extends DefensiveEquipment{

    public Shield() {
    }

    public Shield(String name, int defense) {
        super(name, defense);
    }

    public String getType() {
        return "Shield";
    }
}
