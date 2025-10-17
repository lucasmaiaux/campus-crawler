package fr.campus.dungeoncrawlerapi.domain.game.board.cells.enemies;

import fr.campus.dungeoncrawlerapi.domain.characters.monsters.Monster;
import fr.campus.dungeoncrawlerapi.domain.characters.players.Player;
import fr.campus.dungeoncrawlerapi.domain.game.Game;
import fr.campus.dungeoncrawlerapi.domain.game.board.cells.Cell;
import fr.campus.dungeoncrawlerapi.domain.game.dice.Dice;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Scanner;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("CellEnemy")
public class CellEnemy extends Cell {

    @ManyToOne
    @JoinColumn(name = "monster_id")
    private Monster enemy;

    //private int monsterHealth;
    static Scanner clavier = new Scanner(System.in);

    public CellEnemy() {
    }

    public CellEnemy(Monster enemy) {
        this.enemy = enemy;
    }

    public void interact(Player player, Game game) {
        if (monsterHealth <= 0) {
            game.addLog("Vous enjambez le cadavre d'un monstre mort");
        }
        else {
            fightEnemy(player, game);
        }
    }

    protected void fightEnemy(Player player, Game game) {
        Dice dice = new Dice();
        int playerHealth = player.getHealth();
        int playerAttack = player.getTotalAttack();
        int enemyHealth = monsterHealth;
        int enemyAttack = enemy.getBase_attack();
        int playerDefense = player.getTotalDefense();

        game.addLog("⚔️ Combat entre " + player.getName() + " et " + enemy.getName());
        game.addLog("Vos stats: " + playerHealth + " HP, " + playerAttack + " ATK, " + playerDefense + " DEF");
        game.addLog("Ennemi: " + enemyHealth + " HP, " + enemyAttack + " ATK");

        do {
            int playerAttackFinal = playerAttack;

            int diceRoll = dice.newRoll(20);

            if (diceRoll == 20) {
                // Réussite critique
                game.addLog("🎯 Réussite critique ! (+3 Force)");
                playerAttackFinal = playerAttackFinal + 3;
            }
            else if (diceRoll >= 15) {
                // Réussite normale
                game.addLog("✅ Bon coup ! (+1 Force)");
                playerAttackFinal = playerAttackFinal + 1;
            }
            else if (diceRoll <= 3) {
                // Échec critique
                game.addLog("💥 Échec critique ! (Raté)");
                playerAttackFinal = 0;
            }
            else if (diceRoll <= 8) {
                // Échec partiel
                game.addLog("⚠️ Coup faible (-2 Force)");
                playerAttackFinal = Math.max(0, playerAttackFinal - 2);
            }

            enemyHealth = enemyHealth - playerAttackFinal;
            game.addLog(player.getName() + " tape et inflige " + playerAttackFinal + " DMG");

            if (enemyHealth <= 0) {
                monsterHealth = 0;
                game.addLog(enemy.getName() + " meurt");

                // Attribution d'expérience basée sur le type d'ennemi
                int expGained = 0;
                switch (enemy.getType()) {
                    case "Goblin":
                        expGained = 20;
                        break;
                    case "Orc":
                        expGained = 35;
                        break;
                    case "Sorcière":
                        expGained = 50;
                        break;
                    case "Dragon":
                        expGained = 100;
                        break;
                    default:
                        expGained = 25;
                }

                player.gainExperience(expGained);
                game.addLog("🎉 Victoire ! Vous gagnez " + expGained + " expérience !");
                game.addLog("📊 Niveau " + player.getLevel() + " | Exp: " + player.getExperience() + "/" + player.getExperienceToNextLevel());
            }
            else {
                monsterHealth = enemyHealth;

                // Attaque de l'ennemi avec système d'esquive
                int enemyDiceRoll = dice.newRoll(20);
                int enemyDamage = enemyAttack;

                if (enemyDiceRoll <= 5) {
                    game.addLog("🛡️ Vous esquivez l'attaque !");
                    enemyDamage = 0;
                } else if (enemyDiceRoll >= 18) {
                    game.addLog("💥 Coup critique de l'ennemi !");
                    enemyDamage = enemyAttack + 3;
                }

                // Application de la défense
                int finalDamage = Math.max(0, enemyDamage - playerDefense);
                playerHealth = playerHealth - finalDamage;

                game.addLog("⚔️ " + enemy.getName() + " tape et inflige " + finalDamage + " DMG");

                if (playerDefense > 0 && enemyDamage > 0) {
                    game.addLog("🛡️ Votre défense a absorbé " + (enemyDamage - finalDamage) + " dégâts");
                }

                if (playerHealth <= 0) {
                    player.setHealth(0);
                    game.addLog("VOUS ETES MORT");
                    game.setStatus_code(2);
                    player.setDead(true);
                }
                else  {
                    player.setHealth(playerHealth);
                    game.addLog("❤️ Il vous reste " + playerHealth + " HP");
                }
            }

        } while (enemyHealth >= 0 && !player.isDead());
    }

    public String toString() {
        return "\n[Ennemi : " + enemy.toString() + "]";
    }
}

