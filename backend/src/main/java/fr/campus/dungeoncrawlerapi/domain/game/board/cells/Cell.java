package fr.campus.dungeoncrawlerapi.domain.game.board.cells;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.campus.dungeoncrawlerapi.domain.characters.players.Player;
import fr.campus.dungeoncrawlerapi.domain.game.Game;
import fr.campus.dungeoncrawlerapi.domain.game.board.Board;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cell")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class Cell {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    //(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    @JsonIgnore
    private Board board;

    protected int position;

    protected boolean isEmpty;
    protected int monsterHealth;

    public Cell() {
        this.isEmpty = false;
    }

    public String getType() {
        return this.getClass().getSimpleName();
    }

    public abstract String toString();
    public abstract void interact(Player player, Game game);
}
