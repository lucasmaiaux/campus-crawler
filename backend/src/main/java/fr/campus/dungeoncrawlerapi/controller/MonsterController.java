package fr.campus.dungeoncrawlerapi.controller;

import fr.campus.dungeoncrawlerapi.domain.characters.monsters.Monster;
import fr.campus.dungeoncrawlerapi.dto.MonsterDTO;
import fr.campus.dungeoncrawlerapi.service.MonsterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * GET    /monsters       - Liste des monstres
 * POST   /monsters       - Créer un monstre
 */
@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class MonsterController {

    private final MonsterService monsterService;

    @Autowired
    public MonsterController(MonsterService monsterService) {
        this.monsterService = monsterService;
    }

    // [GET] /monsters : Liste des monstres
    @GetMapping("/monsters")
    public Iterable<Monster> getMonsters() {
        return monsterService.getMonsters();
    }

    // [POST] /monsters : Créer un monstre
    @PostMapping("/monsters")
    public Monster addMonster(@RequestBody MonsterDTO monsterDTO) {
        return monsterService.createMonsterFromDTO(monsterDTO);
    }
}
