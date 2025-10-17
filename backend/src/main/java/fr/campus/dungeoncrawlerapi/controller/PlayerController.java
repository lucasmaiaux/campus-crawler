package fr.campus.dungeoncrawlerapi.controller;

import fr.campus.dungeoncrawlerapi.domain.characters.players.Player;
import fr.campus.dungeoncrawlerapi.dto.PlayerFullDTO;
import fr.campus.dungeoncrawlerapi.dto.PlayerNameDTO;
import fr.campus.dungeoncrawlerapi.dto.PlayerResponse;
import fr.campus.dungeoncrawlerapi.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * GET    /players          - Liste des joueurs
 * POST   /players          - Créer un joueur (Infos completes)
 * POST   /players/name     - Créer un joueur (Pseudo uniquement)
 * GET    /players/{id}     - Détails d'un joueur
 * PUT    /players/{id}     - Modifier un joueur
 * DELETE /players/{id}     - Supprimer un joueur
 */
@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    // [GET] /players : Liste des joueurs
    @GetMapping("/players")
    public Iterable<Player> getPlayers(){
        return playerService.getPlayers();
    }

    // [POST] /players : Créer un joueur (Infos completes)
    @PostMapping("/players")
    public Player addPlayerFull(@RequestBody PlayerFullDTO playerFullDTO) {
        return playerService.createPlayerFromFullDTO(playerFullDTO);
    }

    // [POST] /players/name : Créer un joueur (Pseudo uniquement)
    @PostMapping("/players/name")
    public Player addPlayerName(@RequestBody PlayerNameDTO playerNameDTO) {
        return playerService.createPlayerFromNameDTO(playerNameDTO);
    }

    // [GET] /players/{id} : Détails d'un joueur
    @GetMapping("/players/{id}")
    public Optional<Player> getPlayer(@PathVariable Integer id) {
        return playerService.getPlayer(id);
    }

    // [PUT] /players/{id} : Modifier un joueur
    @PutMapping("/players/{id}")
    public Player updatePlayer(@PathVariable Integer id, @RequestBody PlayerFullDTO playerFullDTO) {
        return playerService.updatePlayerFromDTO(id, playerFullDTO);
    }

    // [DELETE] /players/{id} : Supprimer un joueur
    @DeleteMapping("/players/{id}")
    public void deletePlayer(@PathVariable Integer id) {
        playerService.deletePlayerById(id);
    }
}
