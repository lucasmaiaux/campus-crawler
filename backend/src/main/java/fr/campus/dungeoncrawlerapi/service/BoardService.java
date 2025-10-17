package fr.campus.dungeoncrawlerapi.service;

import fr.campus.dungeoncrawlerapi.domain.characters.monsters.*;
import fr.campus.dungeoncrawlerapi.domain.equipments.defensive.DefensiveEquipment;
import fr.campus.dungeoncrawlerapi.domain.equipments.offensive.OffensiveEquipment;
import fr.campus.dungeoncrawlerapi.domain.equipments.offensive.Spell;
import fr.campus.dungeoncrawlerapi.domain.equipments.offensive.Weapon;
import fr.campus.dungeoncrawlerapi.domain.game.board.Board;
import fr.campus.dungeoncrawlerapi.domain.game.board.cells.Cell;
import fr.campus.dungeoncrawlerapi.domain.game.board.cells.defensive.CellArmor;
import fr.campus.dungeoncrawlerapi.domain.game.board.cells.empty.CellEmpty;
import fr.campus.dungeoncrawlerapi.domain.game.board.cells.enemies.CellEnemy;
import fr.campus.dungeoncrawlerapi.domain.game.board.cells.offensive.CellSpell;
import fr.campus.dungeoncrawlerapi.domain.game.board.cells.offensive.CellWeapon;
import fr.campus.dungeoncrawlerapi.dto.BoardDTO;
import fr.campus.dungeoncrawlerapi.repository.BoardRepository;
import fr.campus.dungeoncrawlerapi.repository.DefensiveEquipmentRepository;
import fr.campus.dungeoncrawlerapi.repository.MonsterRepository;
import fr.campus.dungeoncrawlerapi.repository.OffensiveEquipmentRepository;
import fr.campus.dungeoncrawlerapi.validator.BoardValidator;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * BoardService
 * ├── createBoard(configuration)
 * ├── getBoard(boardId)
 * └── updateBoard(boardId, changes)
 */
@Data
@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MonsterRepository monsterRepository;

    @Autowired
    private OffensiveEquipmentRepository offensiveEquipmentRepository;

    @Autowired
    private DefensiveEquipmentRepository defensiveEquipmentRepository;

    @Autowired
    private BoardValidator boardValidator;

    public Iterable<Board> getBoards() {
        return boardRepository.findAll();
    }

    public Optional<Board> getBoard(Integer boardId) {
        return boardRepository.findById(boardId);
    }

    public Board createBoardFromDTO(BoardDTO boardDTO) {

        // 1) Validation
        boardValidator.validate(boardDTO);

        // 2) Créer le board vide et le sauvegarder d'abord pour obtenir un ID
        Board board = new Board();
        board = boardRepository.save(board);

        // 3) Créer les cellules et les associer au board
        List<Cell> cells = new ArrayList<>(boardDTO.boardSize);
        for (int i = 0; i < boardDTO.boardSize; i++) {
            CellEmpty empty = new CellEmpty();
            empty.setBoard(board);
            empty.setPosition(i);
            empty.setEmpty(true);
            cells.add(empty);
        }
        board.setCells(cells);

        // 4) Charger les "catalogues" depuis la BDD
        List<Monster> monsters = toList(monsterRepository.findAll());
        List<Monster> dragons = monsters.stream().filter(m -> m instanceof Dragon).toList();
        List<Monster> witches = monsters.stream().filter(m -> m instanceof Witch).toList();
        List<Monster> goblins = monsters.stream().filter(m -> m instanceof Goblin).toList();
        List<Monster> orcs    = monsters.stream().filter(m -> m instanceof Orc).toList();

        List<OffensiveEquipment> offs = toList(offensiveEquipmentRepository.findAll());
        List<Weapon> weapons = offs.stream().filter(o -> o instanceof Weapon).map(o -> (Weapon) o).toList();
        List<Spell>  spells  = offs.stream().filter(o -> o instanceof Spell).map(o -> (Spell) o).toList();

        List<DefensiveEquipment> defensives = toList(defensiveEquipmentRepository.findAll());

        // 5) Placement des contenus (mêmes boucles qu'aujourd'hui, mais avec des références EXISTANTES)
        Random r = new Random();

        placeEnemies(board, cells, boardDTO.boardSize, boardDTO.cellsEnemyDragon, dragons, r);
        placeEnemies(board, cells, boardDTO.boardSize, boardDTO.cellsEnemyWitch,  witches, r);
        placeEnemies(board, cells, boardDTO.boardSize, boardDTO.cellsEnemyGoblin, goblins, r);
        placeEnemies(board, cells, boardDTO.boardSize, boardDTO.cellsEnemyOrc,    orcs,    r);

        placeWeapons(board, cells, boardDTO.boardSize, boardDTO.cellsWeaponHammer + boardDTO.cellsWeaponSword + boardDTO.cellsWeaponAxe + boardDTO.cellsWeaponLegendary, weapons, r);
        placeSpells(board, cells, boardDTO.boardSize, boardDTO.cellsSpellThunderstorm + boardDTO.cellsSpellFireball + boardDTO.cellsSpellMeteor, spells, r);

        placeDefensives(board, cells, boardDTO.boardSize,boardDTO.cellsArmor + boardDTO.cellsHelmet + boardDTO.cellsShield, defensives, r);

        // 6) Sauvegarder le board avec toutes ses cellules (cascade = ALL depuis Board vers Cells)
        return boardRepository.save(board);
    }

    public void deleteBoard(Integer boardId) {
        boardRepository.deleteById(boardId);
    }

    // Helpers

    private static <T> List<T> toList(Iterable<T> it) {
        List<T> list = new ArrayList<>();
        it.forEach(list::add);
        return list;
    }

    private void placeEnemies(Board board, List<Cell> cells, int boardSize, int count, List<Monster> pool, Random r) {
        if (count <= 0) return;
        if (pool.isEmpty()) throw new IllegalArgumentException("Catalogue de monstres vide pour ce type");
        while (count > 0) {
            int index = r.nextInt(boardSize - 2) + 1; // évite les bords si tu veux
            if (cells.get(index) instanceof CellEmpty) {
                Monster ref = pool.get(r.nextInt(pool.size()));
                CellEnemy cell = new CellEnemy(ref);
                cell.setMonsterHealth(ref.getMaxHealth());
                cell.setBoard(board);
                cell.setPosition(index);
                cell.setEmpty(false);
                cells.set(index, cell);
                count--;
            }
        }
    }

    private void placeWeapons(Board board, List<Cell> cells, int boardSize, int count, List<Weapon> pool, Random r) {
        if (count <= 0) return;
        if (pool.isEmpty()) throw new IllegalArgumentException("Catalogue d'armes vide");
        while (count > 0) {
            int index = r.nextInt(boardSize - 2) + 1;
            if (cells.get(index) instanceof CellEmpty) {
                Weapon ref = pool.get(r.nextInt(pool.size()));
                CellWeapon cell = new CellWeapon(ref);
                cell.setBoard(board);
                cell.setPosition(index);
                cell.setEmpty(false);
                cells.set(index, cell);
                count--;
            }
        }
    }

    private void placeSpells(Board board, List<Cell> cells, int boardSize, int count, List<Spell> pool, Random r) {
        if (count <= 0) return;
        if (pool.isEmpty()) throw new IllegalArgumentException("Catalogue de sorts vide");
        while (count > 0) {
            int index = r.nextInt(boardSize - 2) + 1;
            if (cells.get(index) instanceof CellEmpty) {
                Spell ref = pool.get(r.nextInt(pool.size()));
                CellSpell cell = new CellSpell(ref);
                cell.setBoard(board);
                cell.setPosition(index);
                cell.setEmpty(false);
                cells.set(index, cell);
                count--;
            }
        }
    }

    private void placeDefensives(Board board, List<Cell> cells, int boardSize, int count, List<DefensiveEquipment> pool, Random r) {
        if (count <= 0) return;
        if (pool.isEmpty()) throw new IllegalArgumentException("Catalogue d'équipements défensifs vide");
        while (count > 0) {
            int index = r.nextInt(boardSize - 2) + 1;
            if (cells.get(index) instanceof CellEmpty) {
                DefensiveEquipment ref = pool.get(r.nextInt(pool.size()));
                CellArmor cell = new CellArmor(ref);
                cell.setBoard(board);
                cell.setPosition(index);
                cell.setEmpty(false);
                cells.set(index, cell);
                count--;
            }
        }
    }
    
}
