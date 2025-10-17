export interface Player {
  id: number;
  name: string;
  type: 'Warrior' | 'Wizard';
  health: number;
  maxHealth: number;
  attack: number;
  base_attack: number;
  defense: number;
  base_defense: number;
  offensiveEquipment?: Equipment;
  defensiveEquipmentHelmet?: Equipment;
  defensiveEquipmentArmor?: Equipment;
  defensiveEquipmentShield?: Equipment;
}

export interface Equipment {
  id: number;
  name: string;
  type: string;
  attackBonus?: number;
  defenseBonus?: number;
  attack?: number;
  defense?: number;
}

export interface Cell {
  type: 'CellEmpty' | 'CellEnemy' | 'CellWeapon' | 'CellSpell' | 'CellArmor' | 'CellPotion';
  position: number;
}

export interface GameState {
  id: number;
  player: Player;
  board: { cells: Cell[] };
  playerPosition: number;
  logs: string;
  status_code: number; // 0: En cours, 1: Gagnée, 2: Perdue
  isGameOver?: boolean; // Deprecated - keep for compatibility
  gameWon?: boolean; // Deprecated - keep for compatibility
}

export interface GameLog {
  id: number;
  message: string;
  timestamp: string;
}

export interface CreateGameRequest {
  playerId: number;
  boardId: number;
}

export interface Board {
  id: number;
  boardSize: number;
  cells: Cell[];
}

export interface PlayerDTO {
  type: 'Warrior' | 'Wizard';
  name: string;
  health: number;
  maxHealth: number;
  attack: number;
  offensiveEquipmentId: number;
  defensiveEquipmentId: number;
}

export interface PlayerNameDTO {
  type: 'Warrior' | 'Wizard';
  name: string;
}

export interface BoardDTO {
  boardSize: number;
  cellsEnemyDragon: number;
  cellsEnemyWitch: number;
  cellsEnemyGoblin: number;
  cellsEnemyOrc: number;
  cellsWeaponHammer: number;
  cellsWeaponSword: number;
  cellsWeaponAxe: number;
  cellsWeaponLegendary: number;
  cellsSpellThunderstorm: number;
  cellsSpellFireball: number;
  cellsSpellMeteor: number;
  cellsArmor: number;
  cellsHelmet: number;
  cellsShield: number;
}

// Game status helpers
export const GameStatus = {
  IN_PROGRESS: 0,
  WON: 1,
  LOST: 2
} as const;

export const isGameOver = (gameState: GameState): boolean => {
  return gameState.status_code !== GameStatus.IN_PROGRESS;
};

export const isGameWon = (gameState: GameState): boolean => {
  return gameState.status_code === GameStatus.WON;
};

export const isGameLost = (gameState: GameState): boolean => {
  return gameState.status_code === GameStatus.LOST;
};

