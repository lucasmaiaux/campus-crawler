package fr.campus.dungeoncrawlerapi.repository;

import fr.campus.dungeoncrawlerapi.domain.characters.players.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Integer> {
}
