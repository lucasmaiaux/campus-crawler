package fr.campus.dungeoncrawlerapi.repository;

import fr.campus.dungeoncrawlerapi.domain.characters.monsters.Monster;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonsterRepository extends CrudRepository<Monster, Integer> {
}
