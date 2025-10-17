package fr.campus.dungeoncrawlerapi.domain.game.dice;

import lombok.Data;

@Data
public class Dice {
    private int value;

    public Dice() {
        // Initialisation à 0 ?
        this.value = 0;
    }

    public String toString() {
        return "Dice{" +
                "value=" + value +
                '}';
    }

    public int newRoll(int faces) {
        return (int)(Math.random() * faces) + 1;
    }

    public int newFakeRoll() {
        return 1;
    }
}