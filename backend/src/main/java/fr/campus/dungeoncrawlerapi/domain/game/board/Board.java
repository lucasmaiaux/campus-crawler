package fr.campus.dungeoncrawlerapi.domain.game.board;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.campus.dungeoncrawlerapi.domain.characters.monsters.Dragon;
import fr.campus.dungeoncrawlerapi.domain.characters.monsters.Goblin;
import fr.campus.dungeoncrawlerapi.domain.characters.monsters.Orc;
import fr.campus.dungeoncrawlerapi.domain.characters.monsters.Witch;
import fr.campus.dungeoncrawlerapi.domain.equipments.consummables.Potion;
import fr.campus.dungeoncrawlerapi.domain.equipments.defensive.Armor;
import fr.campus.dungeoncrawlerapi.domain.equipments.defensive.Helmet;
import fr.campus.dungeoncrawlerapi.domain.equipments.defensive.Shield;
import fr.campus.dungeoncrawlerapi.domain.equipments.offensive.Spell;
import fr.campus.dungeoncrawlerapi.domain.equipments.offensive.Weapon;
import fr.campus.dungeoncrawlerapi.domain.game.Game;
import fr.campus.dungeoncrawlerapi.domain.game.board.cells.Cell;
import fr.campus.dungeoncrawlerapi.domain.game.board.cells.defensive.CellArmor;
import fr.campus.dungeoncrawlerapi.domain.game.board.cells.consummable.CellPotion;
import fr.campus.dungeoncrawlerapi.domain.game.board.cells.empty.CellEmpty;
import fr.campus.dungeoncrawlerapi.domain.game.board.cells.enemies.CellEnemy;
import fr.campus.dungeoncrawlerapi.domain.game.board.cells.offensive.CellSpell;
import fr.campus.dungeoncrawlerapi.domain.game.board.cells.offensive.CellWeapon;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Game> games;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    public List<Cell> cells;

    public Board() {
        this.cells = new ArrayList<Cell>();
        this.games = new ArrayList<Game>();
    }

    public Board(List<Cell> cells) {
        this.cells = cells;
    }

    public Board(int id, List<Cell> cells) {
        this.id = id;
        this.cells = cells;
    }

    public void initBoardRandom(
            int boardSize,
            int cellsEnemyDragon,
            int cellsEnemyWitch,
            int cellsEnemyGoblin,
            int cellsEnemyOrc,

            int cellsWeaponHammer,
            int cellsWeaponSword,
            int cellsWeaponAxe,
            int cellsWeaponLegendary,

            int cellsSpellThunderstorm,
            int cellsSpellFireball,
            int cellsSpellMeteor,

            int cellsShieldWood,
            int cellsShieldSteel,
            int cellsArmorLight,
            int cellsArmorHeavy,
            int cellsHelmetLeather,
            int cellsHelmetSteel,
            int cellsPotion)
    {

        int cellsTotal = cellsEnemyDragon + cellsEnemyWitch + cellsEnemyGoblin + cellsEnemyOrc +
                cellsWeaponHammer + cellsWeaponSword + cellsWeaponAxe + cellsWeaponLegendary +
                cellsSpellThunderstorm + cellsSpellFireball + cellsSpellMeteor +
                cellsShieldWood + cellsShieldSteel +
                cellsArmorLight + cellsArmorHeavy +
                cellsHelmetLeather + cellsHelmetSteel +
                cellsPotion;

        if (cellsTotal > (boardSize - 2)) {
            System.out.println("Pas assez de cases sur le plateau");
        }
        else {
            for (int i = 0; i < boardSize; i++) {
                CellEmpty emptyCell = new CellEmpty();
                emptyCell.setBoard(this);
                emptyCell.setPosition(i);
                emptyCell.setEmpty(false);
                this.getCells().add(emptyCell);
            }

            // Placement des Dragons (boss rares)
            while (cellsEnemyDragon > 0) {
                int index = (int)(Math.random() * (boardSize - 2)) + 1;
                if (cells.get(index) instanceof CellEmpty) {
                    cells.set(index, new CellEnemy(new Dragon("Dragon")));
                    cellsEnemyDragon--;
                }
            }

            // Placement des Sorcières
            while (cellsEnemyWitch > 0) {
                int index = (int)(Math.random() * (boardSize - 2)) + 1;
                if (cells.get(index) instanceof CellEmpty) {
                    cells.set(index, new CellEnemy(new Witch("Sorcière")));
                    cellsEnemyWitch--;
                }
            }

            // Placement des Orcs
            while (cellsEnemyOrc > 0) {
                int index = (int)(Math.random() * (boardSize - 2)) + 1;
                if (cells.get(index) instanceof CellEmpty) {
                    cells.set(index, new CellEnemy(new Orc("Orc")));
                    cellsEnemyOrc--;
                }
            }

            // Placement des Gobelins
            while (cellsEnemyGoblin > 0) {
                int index = (int)(Math.random() * (boardSize - 2)) + 1;
                if (cells.get(index) instanceof CellEmpty) {
                    cells.set(index, new CellEnemy(new Goblin("Gobelin")));
                    cellsEnemyGoblin--;
                }
            }

            // Armes de base (Marteaux)
            while (cellsWeaponHammer > 0) {
                int index = (int)(Math.random() * (boardSize - 2)) + 1;
                if (cells.get(index) instanceof CellEmpty) {
                    cells.set(index, new CellWeapon(new Weapon("Marteau de fer", 8)));
                    cellsWeaponHammer--;
                }
            }

            // Armes intermédiaires (Épées)
            while (cellsWeaponSword > 0) {
                int index = (int)(Math.random() * (boardSize - 2)) + 1;
                if (cells.get(index) instanceof CellEmpty) {
                    cells.set(index, new CellWeapon(new Weapon("Épée d'acier", 12)));
                    cellsWeaponSword--;
                }
            }

            // Armes puissantes (Haches)
            while (cellsWeaponAxe > 0) {
                int index = (int)(Math.random() * (boardSize - 2)) + 1;
                if (cells.get(index) instanceof CellEmpty) {
                    cells.set(index, new CellWeapon(new Weapon("Hache de guerre", 16)));
                    cellsWeaponAxe--;
                }
            }

            // Armes légendaires
            while (cellsWeaponLegendary > 0) {
                int index = (int)(Math.random() * (boardSize - 2)) + 1;
                if (cells.get(index) instanceof CellEmpty) {
                    cells.set(index, new CellWeapon(new Weapon("Épée légendaire", 20)));
                    cellsWeaponLegendary--;
                }
            }

            // Sorts de base
            while (cellsSpellThunderstorm > 0) {
                int index = (int)(Math.random() * (boardSize - 2)) + 1;
                if (cells.get(index) instanceof CellEmpty) {
                    cells.set(index, new CellSpell(new Spell("Éclair", 8)));
                    cellsSpellThunderstorm--;
                }
            }

            // Sorts intermédiaires
            while (cellsSpellFireball > 0) {
                int index = (int)(Math.random() * (boardSize - 2)) + 1;
                if (cells.get(index) instanceof CellEmpty) {
                    cells.set(index, new CellSpell(new Spell("Boule de feu", 12)));
                    cellsSpellFireball--;
                }
            }

            // Sorts puissants
            while (cellsSpellMeteor > 0) {
                int index = (int)(Math.random() * (boardSize - 2)) + 1;
                if (cells.get(index) instanceof CellEmpty) {
                    cells.set(index, new CellSpell(new Spell("Météorite", 18)));
                    cellsSpellMeteor--;
                }
            }

            // Bouclier en bois
            while (cellsShieldWood > 0) {
                int index = (int)(Math.random() * (boardSize - 2)) + 1;
                if (cells.get(index) instanceof CellEmpty) {
                    cells.set(index, new CellArmor(new Shield("Bouclier en bois", 3)));
                    cellsShieldWood--;
                }
            }
            // Bouclier en acier
            while (cellsShieldSteel > 0) {
                int index = (int)(Math.random() * (boardSize - 2)) + 1;
                if (cells.get(index) instanceof CellEmpty) {
                    cells.set(index, new CellArmor(new Shield("Bouclier en acier", 6)));
                    cellsShieldSteel--;
                }
            }
            // Armure légère
            while (cellsArmorLight > 0) {
                int index = (int)(Math.random() * (boardSize - 2)) + 1;
                if (cells.get(index) instanceof CellEmpty) {
                    cells.set(index, new CellArmor(new Armor("Armure légère", 4)));
                    cellsArmorLight--;
                }
            }
            // Armure lourde
            while (cellsArmorHeavy > 0) {
                int index = (int)(Math.random() * (boardSize - 2)) + 1;
                if (cells.get(index) instanceof CellEmpty) {
                    cells.set(index, new CellArmor(new Armor("Armure lourde", 8)));
                    cellsArmorHeavy--;
                }
            }
            // Casque de cuir
            while (cellsHelmetLeather > 0) {
                int index = (int)(Math.random() * (boardSize - 2)) + 1;
                if (cells.get(index) instanceof CellEmpty) {
                    cells.set(index, new CellArmor(new Helmet("Casque de cuir", 2)));
                    cellsHelmetLeather--;
                }
            }
            // Casque d'acier
            while (cellsHelmetSteel > 0) {
                int index = (int)(Math.random() * (boardSize - 2)) + 1;
                if (cells.get(index) instanceof CellEmpty) {
                    cells.set(index, new CellArmor(new Helmet("Casque d'acier", 4)));
                    cellsHelmetSteel--;
                }
            }
            /*
            // Potions de soin (consommables)
            while (cellsPotion > 0) {
                int index = (int)(Math.random() * (boardSize - 2)) + 1;
                if (cells.get(index) instanceof CellEmpty) {
                    cells.set(index, new CellPotion(new Potion("Potion", "Potion de soin", 15)));
                    cellsPotion--;
                }
            }
            */

            System.out.println("Creation d'un plateau de " + cells.size() + " cases");
        }

    }

    public int getSize() {
        return this.cells.size();
    }

    @Override
    public String toString() {
        return "Plateau n°" + id + "\n" + cells;
    }
}
