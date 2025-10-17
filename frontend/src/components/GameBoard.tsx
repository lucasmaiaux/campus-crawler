import React, { useEffect, useRef } from 'react';
import { Card } from '@/components/ui/card';
import { Cell } from '@/types/game';
import { cn } from '@/lib/utils';

interface GameBoardProps {
  board: Cell[];
  currentPosition: number;
}

const getCellIcon = (cell: any): string => {
  // Essayer différentes propriétés pour déterminer le type
  const cellType = cell.type || cell.discriminatorValue || cell.class || '';
  
  
  switch (cellType) {
    case 'CellEmpty': return '·';
    case 'CellEnemy': return '👹';
    case 'CellWeapon': return '⚔️';
    case 'CellSpell': return '✨';
    case 'CellArmor': return '🛡️';
    case 'CellPotion': return '🧪';
    default: 
      // Fallback basé sur les propriétés de la cellule
      if (cell.isEmpty === true) return '·';
      if (cell.monsterHealth > 0) return '👹';
      if (cell.weapon) return '⚔️';
      if (cell.spell) return '✨';
      if (cell.armor) return '🛡️';
      if (cell.potion) return '🧪';
      return '?';
  }
};

const getCellColor = (cell: any): string => {
  const cellType = cell.type || cell.discriminatorValue || cell.class || '';
  
  switch (cellType) {
    case 'CellEmpty': return 'bg-cell-empty text-foreground';
    case 'CellEnemy': return 'bg-cell-enemy text-white';
    case 'CellWeapon': return 'bg-cell-weapon text-white';
    case 'CellSpell': return 'bg-cell-spell text-white';
    case 'CellArmor': return 'bg-cell-armor text-white';
    case 'CellPotion': return 'bg-cell-potion text-primary-foreground';
    default: 
      // Fallback basé sur les propriétés de la cellule
      if (cell.isEmpty === true) return 'bg-cell-empty text-foreground';
      if (cell.monsterHealth > 0) return 'bg-cell-enemy text-white';
      if (cell.weapon) return 'bg-cell-weapon text-white';
      if (cell.spell) return 'bg-cell-spell text-white';
      if (cell.armor) return 'bg-cell-armor text-white';
      if (cell.potion) return 'bg-cell-potion text-primary-foreground';
      return 'bg-muted text-muted-foreground';
  }
};

export const GameBoard: React.FC<GameBoardProps> = ({ board, currentPosition }) => {
  const scrollContainerRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (scrollContainerRef.current) {
      const container = scrollContainerRef.current;
      const cellWidth = 36; // 32px min-width + 4px gap approximativement
      const paddingLeft = 32; // pl-8 = 32px
      const paddingRight = 32; // Padding de sécurité à droite
      const containerWidth = container.clientWidth;
      const totalWidth = board.length * cellWidth + paddingLeft + paddingRight;
      const maxScroll = Math.max(0, totalWidth - containerWidth);

      // Si le contenu est plus petit que le conteneur, pas de scroll nécessaire
      if (maxScroll <= 0) {
        return;
      }

      let scrollPosition: number;

      // Calculer la position idéale pour centrer le joueur (en tenant compte du padding)
      const playerPixelPosition = paddingLeft + (currentPosition * cellWidth);
      const centerOffset = containerWidth / 2;
      const idealScrollPosition = playerPixelPosition - centerOffset;

      // Si on est à la dernière position (fin du jeu)
      if (currentPosition === board.length - 1) {
        scrollPosition = maxScroll;
      }
      // Si la position idéale dépasse le scroll maximum
      else if (idealScrollPosition >= maxScroll) {
        scrollPosition = maxScroll;
      }
      // Si on est au début
      else if (idealScrollPosition <= 0) {
        scrollPosition = 0;
      }
      // Position normale au centre
      else {
        scrollPosition = idealScrollPosition;
      }

      container.scrollTo({
        left: scrollPosition,
        behavior: 'smooth'
      });
    }
  }, [currentPosition, board.length]);

  return (
    <Card className="bg-gradient-card shadow-card border-border overflow-visible">
      <div className="p-6">
        <div className="text-center mb-4">
          <h3 className="text-xl font-semibold text-foreground mb-2">Plateau de jeu</h3>
          <p className="text-sm text-muted-foreground">Position actuelle: {currentPosition}</p>
        </div>

        <div className="relative">
          <div
            ref={scrollContainerRef}
            className="flex gap-1 overflow-x-auto pb-2 pt-10 pl-8 scrollbar-thin scrollbar-thumb-border scrollbar-track-background overflow-visible"
          >
            {board.map((cell, index) => {
              const isPlayerPosition = index === currentPosition;
              const isLastCell = index === board.length - 1;
              const cellColor = getCellColor(cell);
              const detectedType = cell.type || cell.discriminatorValue || cell.class || 'Unknown';

              return (
                <div
                  key={index}
                  className={cn(
                    "min-w-[32px] h-8 flex items-center justify-center text-sm font-medium border-2 transition-all duration-300",
                    cellColor,
                    isPlayerPosition
                      ? "border-player-position shadow-[0_0_10px_hsl(var(--player-position))] scale-110 z-10"
                      : "border-border hover:border-accent/50"
                  )}
                  title={`Position ${index}: ${isLastCell ? 'Arrivée' : detectedType}`}
                >
                  <span className={cn(
                    "select-none transition-transform duration-200",
                    isPlayerPosition && "scale-125"
                  )}>
                    {isLastCell ? '🏁' : getCellIcon(cell)}
                  </span>
                  {isPlayerPosition && (
                    <div className="absolute -top-9 left-1/2 transform -translate-x-1/2 z-50">
                      <div className="bg-player-position text-primary-foreground px-2 py-1 rounded text-xs font-bold shadow-lg whitespace-nowrap">
                        Joueur
                      </div>
                      <div className="w-0 h-0 border-l-4 border-r-4 border-t-4 border-transparent border-t-player-position mx-auto"></div>
                    </div>
                  )}
                </div>
              );
            })}
          </div>
        </div>
        
        <div className="mt-4 flex flex-wrap gap-2 justify-center text-xs">
          <div className="flex items-center gap-1">
            <div className="w-4 h-4 bg-cell-empty border border-border rounded"></div>
            <span className="text-muted-foreground">Vide</span>
          </div>
          <div className="flex items-center gap-1">
            <div className="w-4 h-4 bg-cell-enemy border border-border rounded"></div>
            <span className="text-muted-foreground">Ennemi</span>
          </div>
          <div className="flex items-center gap-1">
            <div className="w-4 h-4 bg-cell-weapon border border-border rounded"></div>
            <span className="text-muted-foreground">Arme</span>
          </div>
          <div className="flex items-center gap-1">
            <div className="w-4 h-4 bg-cell-spell border border-border rounded"></div>
            <span className="text-muted-foreground">Sort</span>
          </div>
          <div className="flex items-center gap-1">
            <div className="w-4 h-4 bg-cell-armor border border-border rounded"></div>
            <span className="text-muted-foreground">Armure</span>
          </div>
        </div>
      </div>
    </Card>
  );
};