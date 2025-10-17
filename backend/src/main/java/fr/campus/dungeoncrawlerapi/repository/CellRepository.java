package fr.campus.dungeoncrawlerapi.repository;

import fr.campus.dungeoncrawlerapi.domain.game.board.cells.Cell;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CellRepository extends CrudRepository<Cell, Integer> {
}
