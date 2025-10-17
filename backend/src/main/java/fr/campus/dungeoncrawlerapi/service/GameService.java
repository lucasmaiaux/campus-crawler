package fr.campus.dungeoncrawlerapi.service;

import fr.campus.dungeoncrawlerapi.domain.characters.players.Player;
import fr.campus.dungeoncrawlerapi.domain.game.Game;
import fr.campus.dungeoncrawlerapi.domain.game.board.Board;
import fr.campus.dungeoncrawlerapi.repository.BoardRepository;
import fr.campus.dungeoncrawlerapi.repository.GameRepository;
import fr.campus.dungeoncrawlerapi.repository.PlayerRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 * GameService
 * ├── createGame(playerId, boardId)
 * ├── getGameState(gameId)
 * ├── movePlayer(gameId, direction)
 * ├── performAction(gameId, actionType, target)
 * └── endGame(gameId)
 */
@Data
@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private BoardRepository boardRepository;

    public Iterable<Game> getGames() {
        return gameRepository.findAll();
    }

    public Optional<Game> getGame(Integer gameId) {
        return gameRepository.findById(gameId);
    }

    public List<String> getLogs(Integer gameId) {
        var game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));

        return game.getLogsList();
    }

    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }

    public Game createGame(Integer playerId, Integer boardId) {
        // Récupérer le joueur et le board depuis leurs repositories
        // Créer une nouvelle instance de Game
        // Sauvegarder et retourner
        Player player = playerRepository.findById(playerId).get();
        Board board = boardRepository.findById(boardId).get();
        Game game = new Game(1, player, 0 , board);
        return gameRepository.save(game);
    }

    public void deleteGame(Integer gameId) {
        gameRepository.deleteById(gameId);
    }

    @Transactional
    public Game move(Integer id) {

        var game = gameRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));

        if (game.getStatus_code() == 0) {
            int diceRoll = (int)(Math.random() * 6) + 1;
            int maxCell = game.getBoard().getSize() - 1;
            int playerNextPosition = Math.min(game.getPlayerPosition() + diceRoll, maxCell);

            game.addLog("Déplacement " + game.getPlayerPosition() + " -> " + playerNextPosition);
            game.setPlayerPosition(playerNextPosition);

            if (game.getPlayerPosition() == game.getBoard().getSize() - 1) {
                game.addLog("Partie terminée");
                game.setStatus_code(1);
            }
        }

        return gameRepository.save(game);
    }

    @Transactional
    public Game interact(Integer id) {

        var game = gameRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));

        if (game.getPlayerPosition() < game.getBoard().getSize() - 1) {
            game.getBoard().cells.get(game.getPlayerPosition()).interact(game.getPlayer(), game);
        }

        return gameRepository.save(game);
    }
}
