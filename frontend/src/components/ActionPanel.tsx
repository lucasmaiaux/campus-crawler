import React from 'react';
import { Card } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Loader2, ArrowRight, Hand } from 'lucide-react';

interface ActionPanelProps {
  onMove: () => void;
  onInteract: () => void;
  isLoading: boolean;
  isGameOver?: boolean;
  gameWon?: boolean;
  gameId?: number;
}

export const ActionPanel: React.FC<ActionPanelProps> = ({
  onMove,
  onInteract,
  isLoading,
  isGameOver = false,
  gameWon,
  gameId
}) => {
  return (
    <Card className="bg-gradient-card shadow-card border-border">
      <div className="p-6">
        <div className="text-center mb-4">
          <h3 className="text-xl font-semibold text-foreground mb-2">Actions</h3>
          {isGameOver ? (
            <div className="space-y-2">
              <div className={`text-lg font-bold ${gameWon ? 'text-success' : 'text-destructive'}`}>
                {gameWon ? '🎉 Victoire!' : '💀 Défaite'}
              </div>
              <p className="text-sm text-muted-foreground">
                {gameWon ? 'Félicitations, vous avez terminé le donjon!' : 'Votre aventure se termine ici...'}
              </p>
            </div>
          ) : (
            <p className="text-sm text-muted-foreground">Choisissez votre action</p>
          )}
        </div>

        {!isGameOver && (
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div className="flex flex-col">
              <Button
                onClick={onMove}
                disabled={isLoading}
                className="bg-green-600/20 hover:bg-green-600/30 border-2 border-green-500/30 text-green-300 transition-all hover:border-green-400/50"
                size="lg"
              >
                {isLoading ? (
                  <Loader2 className="w-5 h-5 animate-spin mr-2" />
                ) : (
                  <ArrowRight className="w-5 h-5 mr-2" />
                )}
                Avancer
              </Button>
              <span className="text-xs text-muted-foreground mt-1 text-center opacity-70">
                PUT /games/{gameId || '{id}'}/move
              </span>
            </div>

            <div className="flex flex-col">
              <Button
                onClick={onInteract}
                disabled={isLoading}
                className="bg-yellow-600/20 hover:bg-yellow-600/30 border-2 border-yellow-500/30 text-yellow-300 transition-all hover:border-yellow-400/50"
                size="lg"
              >
                {isLoading ? (
                  <Loader2 className="w-5 h-5 animate-spin mr-2" />
                ) : (
                  <Hand className="w-5 h-5 mr-2" />
                )}
                Interagir
              </Button>
              <span className="text-xs text-muted-foreground mt-1 text-center opacity-70">
                POST /games/{gameId || '{id}'}/interact
              </span>
            </div>
          </div>
        )}
      </div>
    </Card>
  );
};