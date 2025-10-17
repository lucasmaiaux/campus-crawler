package fr.campus.dungeoncrawlerapi.domain.game;

import fr.campus.dungeoncrawlerapi.domain.characters.players.Player;
import fr.campus.dungeoncrawlerapi.domain.game.board.Board;
import fr.campus.dungeoncrawlerapi.domain.game.dice.Dice;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Entity
@Table(name="game")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    private int nbPlayers = 1;
    private int playerPosition;

    // 0 : En cours, 1 : Gagnée, 2 : Perdue
    private int status_code = 0;

    @ManyToOne
            //(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @Transient
    private Dice dice = new Dice();

    @Column(name = "logs", columnDefinition = "TEXT")
    private String logs = "Nouvelle partie";

    private static final int MAX_LOG_LINES = 50;

    public Game() {
    }

    public Game(int nb_players, Player player, int position) {
        this.nbPlayers = nb_players;
        this.player = player;
        this.playerPosition = position;
        this.board = new Board();
        //board.initBoardRandom(64);

        board.initBoardRandom(
                64,
                2,
                4,
                6,
                4,
                6,
                6,
                4,
                2,
                6,
                4,
                2,
                3,
                2,
                3,
                2,
                2,
                2,
                2);
    }

    public Game(int nb_players, Player player, int position, Board board) {
        this.nbPlayers = nb_players;
        this.player = player;
        this.playerPosition = position;
        this.board = board;
    }

    public void addLog(String newLog) {
        logs += "\n" + newLog;
        String[] lines = logs.split("\n");
        if (lines.length > MAX_LOG_LINES) {
            // Garder les 50 dernières lignes
            String[] lastLines = Arrays.copyOfRange(lines, lines.length - MAX_LOG_LINES, lines.length);
            logs = String.join("\n", lastLines);
        }
    }

    public List<String> getLogsList() {
        if (logs.isEmpty()) return new ArrayList<>();
        return Arrays.asList(logs.split("\n"));
    }
}
