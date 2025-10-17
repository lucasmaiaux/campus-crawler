package fr.campus.dungeoncrawlerapi.domain.game.board.cells.empty;

import fr.campus.dungeoncrawlerapi.domain.characters.players.Player;
import fr.campus.dungeoncrawlerapi.domain.game.Game;
import fr.campus.dungeoncrawlerapi.domain.game.board.cells.Cell;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("CellEmpty")
public class CellEmpty extends Cell {

    public CellEmpty() {
    }

    @Override
    public void interact(Player player, Game game) {
        game.addLog("Rien ne se passe");
    }

    @Override
    public String toString() {
        return "\n[Vide]";
    }
}
