package fr.campus.dungeoncrawlerapi.service;

import fr.campus.dungeoncrawlerapi.domain.characters.players.Player;
import fr.campus.dungeoncrawlerapi.domain.characters.players.Warrior;
import fr.campus.dungeoncrawlerapi.domain.characters.players.Wizard;
import fr.campus.dungeoncrawlerapi.dto.PlayerFullDTO;
import fr.campus.dungeoncrawlerapi.dto.PlayerNameDTO;
import fr.campus.dungeoncrawlerapi.repository.DefensiveEquipmentRepository;
import fr.campus.dungeoncrawlerapi.repository.OffensiveEquipmentRepository;
import fr.campus.dungeoncrawlerapi.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import lombok.Data;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * PlayerService
 * ├── createPlayer(playerData)
 * ├── updatePlayer(playerId, playerData)
 * ├── getPlayer(playerId)
 * └── deletePlayer(playerId)
 */
@Data
@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private OffensiveEquipmentRepository offensiveEquipmentRepository;
    @Autowired
    private DefensiveEquipmentRepository defensiveEquipmentRepository;

    public Iterable<Player> getPlayers() {
        return playerRepository.findAll();
    }

    public Optional<Player> getPlayer(Integer id) {
        return playerRepository.findById(id);
    }

    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }
    
    public void deletePlayerById(Integer id) {
        playerRepository.deleteById(id);
    }

    public Player createPlayerFromFullDTO(PlayerFullDTO playerFullDTO) {
        Player player;

        switch (playerFullDTO.type) {
            case "Wizard":
                player = new Wizard();
                break;
            case "Warrior":
                player = new Warrior();
                break;
            default:
                throw new IllegalArgumentException("Type de joueur inconnu : " + playerFullDTO.type);
        }

        player.setName(playerFullDTO.name);
        player.setHealth(playerFullDTO.health);
        player.setMaxHealth(playerFullDTO.maxHealth);
        player.setBase_attack(playerFullDTO.attack);
        player.setBase_defense(0);

        if (playerFullDTO.offensiveEquipmentId > 0) {
            var off = offensiveEquipmentRepository.findById(playerFullDTO.offensiveEquipmentId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "offensiveEquipmentId invalide"));
            player.setOffensiveEquipment(off);
        }
            
        if (playerFullDTO.defensiveEquipmentId > 0) {
            var def = defensiveEquipmentRepository.findById(playerFullDTO.defensiveEquipmentId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "defensiveEquipmentId invalide"));
            player.setDefensiveEquipmentHelmet(def);
        }

        return playerRepository.save(player);
    }

    public Player createPlayerFromNameDTO(PlayerNameDTO playerNameDTO) {
        Player player;

        switch (playerNameDTO.type) {
            case "Wizard":
                player = new Wizard();
                player.setHealth(8);
                player.setMaxHealth(8);
                player.setBase_attack(12);
                player.setBase_defense(0);

                player.setOffensiveEquipment(offensiveEquipmentRepository.findByName("Éclair")
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Équipement offensif par défaut introuvable")));

                player.setDefensiveEquipmentHelmet(defensiveEquipmentRepository.findByName("Casque de cuir")
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Équipement défensif par défaut introuvable")));

                player.setDefensiveEquipmentArmor(defensiveEquipmentRepository.findByName("Armure légère")
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Équipement défensif par défaut introuvable")));

                break;
            case "Warrior":
                player = new Warrior();
                player.setHealth(10);
                player.setMaxHealth(10);
                player.setBase_attack(10);
                player.setBase_defense(0);

                player.setOffensiveEquipment(offensiveEquipmentRepository.findByName("Marteau de fer")
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Équipement offensif par défaut introuvable")));

                player.setDefensiveEquipmentHelmet(defensiveEquipmentRepository.findByName("Casque de cuir")
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Équipement défensif par défaut introuvable")));

                player.setDefensiveEquipmentArmor(defensiveEquipmentRepository.findByName("Armure légère")
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Équipement défensif par défaut introuvable")));

                player.setDefensiveEquipmentShield(defensiveEquipmentRepository.findByName("Bouclier en bois")
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Équipement défensif par défaut introuvable")));

                break;
            default:
                throw new IllegalArgumentException("Type de joueur inconnu : " + playerNameDTO.type);
        }

        player.setName(playerNameDTO.name);


        return playerRepository.save(player);
    }

    public Player updatePlayerFromDTO(Integer id, PlayerFullDTO dto) {
        var player = playerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found"));

        if (dto.name != null) player.setName(dto.name);
        player.setHealth(dto.health);
        player.setMaxHealth(dto.maxHealth);
        player.setBase_attack(dto.attack);
        player.setBase_defense(0);

        if (dto.offensiveEquipmentId > 0) {
            var off = offensiveEquipmentRepository.findById(dto.offensiveEquipmentId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "offensiveEquipmentId invalide"));
            player.setOffensiveEquipment(off);
        }

        if (dto.defensiveEquipmentId > 0) {
            var def = defensiveEquipmentRepository.findById(dto.defensiveEquipmentId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "defensiveEquipmentId invalide"));
            player.setDefensiveEquipmentHelmet(def);
        }

        return playerRepository.save(player);
    }
}
