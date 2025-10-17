package fr.campus.dungeoncrawlerapi.domain.equipments.consummables;

public class Potion extends Consummable {

    public Potion(String name, int defense) {
        super(name, defense);
    }

    public Potion(String type, String name, int defense) {
        super(name, defense);
        this.type = type;
    }

}


