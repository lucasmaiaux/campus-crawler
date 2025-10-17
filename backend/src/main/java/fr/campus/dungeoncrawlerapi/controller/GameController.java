package fr.campus.dungeoncrawlerapi.controller;

import fr.campus.dungeoncrawlerapi.domain.game.Game;
import fr.campus.dungeoncrawlerapi.dto.CreateGameRequest;
import fr.campus.dungeoncrawlerapi.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * GET    /games            - Liste des parties
 * POST   /games            - Créer une nouvelle partie
 * GET    /games/{id}       - État d'une partie
 * DELETE /games/{id}       - Supprimer une partie
 * GET    /games/{id}/logs  - Logs d'une partie
 * PUT    /games/{id}/move  - Effectuer un mouvement
 * POST   /games/{id}/interact - Effectuer une interaction (combat, équipement)
 */
@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    // [GET] /games : Liste des parties
    @GetMapping("/games")
    public Iterable<Game> getGames() {
        return gameService.getGames();
    }

    // [POST] /games : Créer une nouvelle partie
    @PostMapping("/games")
    public ResponseEntity<Game> createGame(@RequestBody CreateGameRequest request) {
        Game game = gameService.createGame(request.getPlayerId(), request.getBoardId());
        return ResponseEntity.status(HttpStatus.CREATED).body(game);
    }

    // [GET] /games/{id} : État d'une partie
    @GetMapping("/games/{id}")
    public Optional<Game> getGame(@PathVariable Integer id) {
        return gameService.getGame(id);
    }

    // [DELETE] /games/{id} : Supprimer une partie
    @DeleteMapping("/games/{id}")
    public void deleteGame(@PathVariable Integer id) {
        gameService.deleteGame(id);
    }

    // [GET] /games/{id}/logs : Logs d'une partie
    @GetMapping("/games/{id}/logs")
    public List<String> getLogs(@PathVariable Integer id) {
        return gameService.getLogs(id);
    }

    // [PUT] /games/{id}/move : Effectuer un mouvement
    @PutMapping("/games/{id}/move")
    public Game move(@PathVariable Integer id) {
        return gameService.move(id);
    }

    // [POST] /games/{id}/interact : Effectuer une interaction (combat, équipement)
    @PostMapping("/games/{id}/interact")
    public Game interact (@PathVariable Integer id) {
        return gameService.interact(id);
    }
}
