package fr.campus.dungeoncrawlerapi.domain.game.board.cells.offensive;

import fr.campus.dungeoncrawlerapi.domain.characters.players.Player;
import fr.campus.dungeoncrawlerapi.domain.characters.players.Wizard;
import fr.campus.dungeoncrawlerapi.domain.equipments.offensive.Spell;
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
@DiscriminatorValue("CellSpell")
public class CellSpell extends Cell {

    @ManyToOne
    @JoinColumn(name = "spell_id")
    private Spell spell;

    public CellSpell() {
    }

    public CellSpell(Spell spell) {
        //this.type = "CellSpell";
        this.spell = spell;
    }

    @Override
    public void interact(Player player, Game game) {

        game.addLog("✨ Vous avez trouvé : " + spell.toString());

        if (player instanceof Wizard) {
            if (player.getOffensiveEquipment().getAttack() < spell.getAttack()) {
                game.addLog("Vous remplacez votre " + player.getOffensiveEquipment().toString());
                player.setOffensiveEquipment(spell);
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
        return "\n[Sort : " + spell.getName() + "]";
    }
}
