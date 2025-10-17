package fr.campus.dungeoncrawlerapi.repository;

import fr.campus.dungeoncrawlerapi.domain.game.board.Board;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends CrudRepository<Board, Integer> {
}
