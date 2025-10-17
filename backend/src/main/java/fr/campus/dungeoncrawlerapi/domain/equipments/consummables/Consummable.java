package fr.campus.dungeoncrawlerapi.domain.equipments.consummables;

import lombok.Data;

@Data
public class Consummable {
    protected String type;
    protected String name;
    protected int defense;

    public Consummable(String name, int defense) {
        this.name = name;
        this.defense = defense;
    }

    @Override
    public String toString() {
        return name + " (" + defense + ")";
    }
}
