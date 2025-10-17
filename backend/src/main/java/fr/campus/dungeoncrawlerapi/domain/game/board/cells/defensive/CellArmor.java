package fr.campus.dungeoncrawlerapi.domain.game.board.cells.defensive;

import fr.campus.dungeoncrawlerapi.domain.characters.players.Player;
import fr.campus.dungeoncrawlerapi.domain.equipments.defensive.DefensiveEquipment;
import fr.campus.dungeoncrawlerapi.domain.game.Game;
import fr.campus.dungeoncrawlerapi.domain.game.board.cells.Cell;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Objects;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("CellArmor")
public class CellArmor extends Cell {

    @ManyToOne
    @JoinColumn(name = "defensive_equipment_id")
    private DefensiveEquipment defensiveEquipment;

    public CellArmor() {
    }

    public CellArmor(DefensiveEquipment defensiveEquipment) {
        //this.type = "CellShield";
        this.defensiveEquipment = defensiveEquipment;
    }

    @Override
    public void interact(Player player, Game game) {

        game.addLog("Vous avez trouvé : " + defensiveEquipment.toString());

        switch (defensiveEquipment.getType()) {
            case "Helmet":
                if (player.getDefensiveEquipmentHelmet().getDefense() < defensiveEquipment.getDefense()) {
                    game.addLog("Vous remplacez votre " + player.getDefensiveEquipmentHelmet().toString());
                    player.setDefensiveEquipmentHelmet(defensiveEquipment);
                }
                else {
                    game.addLog("Vous gardez votre " + player.getDefensiveEquipmentHelmet().toString());
                }
                break;
            case "Armor":
                if (player.getDefensiveEquipmentArmor().getDefense() < defensiveEquipment.getDefense()) {
                    game.addLog("Vous remplacez votre " + player.getDefensiveEquipmentArmor().toString());
                    player.setDefensiveEquipmentArmor(defensiveEquipment);
                }
                else {
                    game.addLog("Vous gardez votre " + player.getDefensiveEquipmentArmor().toString());
                }
                break;
            case "Shield":
                if (Objects.equals(player.getType(), "Wizard")) {
                    game.addLog("Cet objet n'est pas pour votre classe");

                }
                else {
                    if (player.getDefensiveEquipmentShield().getDefense() < defensiveEquipment.getDefense()) {
                        game.addLog("Vous remplacez votre " + player.getDefensiveEquipmentShield().toString());
                        player.setDefensiveEquipmentShield(defensiveEquipment);
                    }
                    else {
                        game.addLog("Vous gardez votre " + player.getDefensiveEquipmentShield().toString());
                    }
                }
        }
    }

    @Override
    public String toString() {
        return "\n[Shield]";
    }
}
