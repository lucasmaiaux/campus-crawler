package fr.campus.dungeoncrawlerapi.validator;

import fr.campus.dungeoncrawlerapi.dto.BoardDTO;
import org.springframework.stereotype.Component;

@Component
public class BoardValidator {

    public void validate(BoardDTO boardDTO) {
        validateBasicConstraints(boardDTO);
        validateCapacity(boardDTO);
        validateElementCounts(boardDTO);
    }

    private void validateBasicConstraints(BoardDTO boardDTO) {
        if (boardDTO.boardSize <= 0) {
            throw new IllegalArgumentException("boardSize doit être > 0");
        }
        if (boardDTO.boardSize > 1000) { // limite max raisonnable
            throw new IllegalArgumentException("boardSize trop grand (max 1000)");
        }
    }

    private void validateCapacity(BoardDTO boardDTO) {
        int totalElements = calculateTotalElements(boardDTO);
        int availableSlots = boardDTO.boardSize - 2;

        if (totalElements > availableSlots) {
            throw new IllegalArgumentException(
                    String.format("Trop d'éléments (%d) pour %d slots disponibles",
                            totalElements, availableSlots));
        }
    }

    private int calculateTotalElements(BoardDTO boardDTO) {
        return boardDTO.cellsEnemyDragon + boardDTO.cellsEnemyWitch + boardDTO.cellsEnemyGoblin + boardDTO.cellsEnemyOrc +
                boardDTO.cellsWeaponHammer + boardDTO.cellsWeaponSword + boardDTO.cellsWeaponAxe + boardDTO.cellsWeaponLegendary +
                boardDTO.cellsSpellFireball + boardDTO.cellsSpellMeteor + boardDTO.cellsSpellThunderstorm +
                boardDTO.cellsArmor + boardDTO.cellsHelmet + boardDTO.cellsShield;
    }

    private void validateElementCounts(BoardDTO boardDTO) {
        if (boardDTO.cellsEnemyDragon < 0) {
            throw new IllegalArgumentException("cellsEnemyDragon ne peut pas être négatif");
        }
    }
}