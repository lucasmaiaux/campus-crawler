package fr.campus.dungeoncrawlerapi.service;

import fr.campus.dungeoncrawlerapi.domain.characters.monsters.*;
import fr.campus.dungeoncrawlerapi.dto.MonsterDTO;
import fr.campus.dungeoncrawlerapi.repository.MonsterRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Service
public class MonsterService {

    @Autowired
    private MonsterRepository monsterRepository;

    public Iterable<Monster> getMonsters() {
        return monsterRepository.findAll();
    }

    public Monster saveMonster(Monster monster) {
        return monsterRepository.save(monster);
    }

    public Monster createMonsterFromDTO(MonsterDTO monsterDTO) {
        Monster monster;

        switch (monsterDTO.type) {
            case "Dragon":
                monster = new Dragon();
                break;
            case "Goblin":
                monster = new Goblin();
                break;
            case "Orc":
                monster = new Orc();
                break;
            case "Witch":
                monster = new Witch();
                break;
            default:
                throw new IllegalArgumentException("Type de monstre inconnu : " + monsterDTO.type);
        }

        monster.setName(monsterDTO.name);
        monster.setMaxHealth(monsterDTO.maxHealth);
        monster.setBase_attack(monsterDTO.attack);

        return monsterRepository.save(monster);
    }
}
