package fr.campus.dungeoncrawlerapi.domain.game.board.cells.consummable;

import fr.campus.dungeoncrawlerapi.domain.characters.players.Player;
import fr.campus.dungeoncrawlerapi.domain.equipments.consummables.Potion;
import fr.campus.dungeoncrawlerapi.domain.game.Game;
import fr.campus.dungeoncrawlerapi.domain.game.board.cells.Cell;

public class CellPotion extends Cell {
    private Potion potion;

    public CellPotion(Potion potion) {
        this.potion = potion;
    }

    public Potion getPotion() {
        return potion;
    }

    public void setPotion(Potion potion) {
        this.potion = potion;
    }

    @Override
    public void interact(Player player, Game game) {

        //System.out.println("\uD83E\uDDEA Vous avez trouvé : " + potion.toString());
        game.addLog("Vous avez trouvé : " + potion.toString());

    }

    @Override
    public String toString() {
        return "\n[Potion]";
    }
}