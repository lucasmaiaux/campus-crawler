package fr.campus.dungeoncrawlerapi.domain.game.board.cells.offensive;

import fr.campus.dungeoncrawlerapi.domain.characters.players.Player;
import fr.campus.dungeoncrawlerapi.domain.characters.players.Warrior;
import fr.campus.dungeoncrawlerapi.domain.equipments.offensive.Weapon;
import fr.campus.dungeoncrawlerapi.domain.game.Game;
import fr.campus.dungeoncrawlerapi.domain.game.board.cells.Cell;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("CellWeapon")
public class CellWeapon extends Cell {

    @ManyToOne
    @JoinColumn(name = "weapon_id")
    private Weapon weapon;

    public CellWeapon() {
    }

    public CellWeapon(Weapon weapon) {
        //this.type = "CellWeapon";
        this.weapon = weapon;
    }

    @Override
    public void interact(Player player, Game game) {

        game.addLog("Vous avez trouvé : " + weapon.toString());

        if (player instanceof Warrior) {
            if (player.getOffensiveEquipment().getAttack() < weapon.getAttack()) {
                player.setOffensiveEquipment(weapon);
                game.addLog("Vous remplacez votre " + player.getOffensiveEquipment().toString());
            }
            else {
                game.addLog("Vous gardez votre " + player.getOffensiveEquipment().toString());
            }
        }
        else {
            game.addLog("Cette arme n'est pas pour votre classe");
        }

    }

    public String toString() {
        return "\n[Arme : " + weapon.getName() + "]";
    }
}