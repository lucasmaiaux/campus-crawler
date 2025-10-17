import React, { useState, useEffect, useCallback } from 'react';
import { GameSetup } from '@/components/GameSetup';
import { GameBoard } from '@/components/GameBoard';
import { PlayerStats } from '@/components/PlayerStats';
import { ActionPanel } from '@/components/ActionPanel';
import { GameLogs } from '@/components/GameLogs';
import { GameState, GameLog, isGameOver, isGameWon } from '@/types/game';
import { gameApi, ApiError } from '@/services/gameApi';
import { toast } from '@/hooks/use-toast';
import { Loader2, RotateCcw } from 'lucide-react';
import { Button } from '@/components/ui/button';

export const Game: React.FC = () => {
  const [gameState, setGameState] = useState<GameState | null>(null);
  const [gameLogs, setGameLogs] = useState<GameLog[]>([]);
  const [isActionLoading, setIsActionLoading] = useState(false);
  const [isLogsLoading, setIsLogsLoading] = useState(false);
  const [isRefreshing, setIsRefreshing] = useState(false);

  const loadGameLogs = useCallback(async (gameId: number) => {
    try {
      setIsLogsLoading(true);
      const logs = await gameApi.getGameLogs(gameId);
      setGameLogs(logs);
    } catch (error) {
      const apiError = error as ApiError;
      console.error('Failed to load game logs:', apiError.message);
    } finally {
      setIsLogsLoading(false);
    }
  }, []);

  const refreshGameState = useCallback(async () => {
    if (!gameState?.id) return;
    
    try {
      setIsRefreshing(true);
      const updatedState = await gameApi.getGameState(gameState.id);
      setGameState(updatedState);
      await loadGameLogs(gameState.id);
    } catch (error) {
      const apiError = error as ApiError;
      toast({
        title: "Erreur de rafraîchissement",
        description: apiError.message || "Impossible de mettre à jour l'état du jeu",
        variant: "destructive",
      });
    } finally {
      setIsRefreshing(false);
    }
  }, [gameState?.id, loadGameLogs]);

  const handleGameCreated = useCallback((newGameState: GameState) => {
    setGameState(newGameState);
    loadGameLogs(newGameState.id);
  }, [loadGameLogs]);

  const handleMove = async () => {
    if (!gameState?.id) return;

    setIsActionLoading(true);
    try {
      const updatedState = await gameApi.movePlayer(gameState.id);
      setGameState(updatedState);
      await loadGameLogs(gameState.id);
      
      toast({
        title: "Mouvement effectué",
        description: `Nouvelle position: ${updatedState.playerPosition}`,
      });
    } catch (error) {
      const apiError = error as ApiError;
      toast({
        title: "Erreur de mouvement",
        description: apiError.message || "Impossible d'effectuer le mouvement",
        variant: "destructive",
      });
    } finally {
      setIsActionLoading(false);
    }
  };

  const handleInteract = async () => {
    if (!gameState?.id) return;

    setIsActionLoading(true);
    try {
      const updatedState = await gameApi.interact(gameState.id);
      setGameState(updatedState);
      await loadGameLogs(gameState.id);
      
      toast({
        title: "Interaction effectuée",
        description: "Action réalisée avec succès",
      });
    } catch (error) {
      const apiError = error as ApiError;
      toast({
        title: "Erreur d'interaction",
        description: apiError.message || "Impossible d'effectuer l'interaction",
        variant: "destructive",
      });
    } finally {
      setIsActionLoading(false);
    }
  };

  const resetGame = () => {
    setGameState(null);
    setGameLogs([]);
  };


  return (
    <div className="h-screen bg-background overflow-hidden">
      <div className="container mx-auto px-4 py-4 h-full flex flex-col">
        {/* Header */}
        <div className="text-center py-2 flex-shrink-0">
          <h1 className="text-3xl font-bold bg-gradient-primary bg-clip-text text-transparent">
            Dungeon Crawler RPG
          </h1>
        </div>

        {!gameState ? (
          <div className="flex-1 flex flex-col min-h-0">
            <div className="flex-1 flex items-start justify-center pt-8">
              <div className="w-full max-w-6xl mx-auto px-4">
                <GameSetup 
                  onGameCreated={handleGameCreated}
                  isLoading={isActionLoading}
                />
              </div>
            </div>
          </div>
        ) : (
          <div className="flex-1 flex flex-col min-h-0">
            {/* Main Game Layout */}
            <div className="grid grid-cols-1 lg:grid-cols-5 gap-4 h-full min-h-0">
              {/* Left Column - Game Info and Player Stats */}
              <div className="lg:col-span-1 space-y-4 flex-shrink-0">
                {/* Game Info */}
                <div className="p-4 bg-card rounded-lg border border-border shadow-card">
                  <div className="text-center space-y-3">
                    <h2 className="text-lg font-semibold text-foreground">
                      Partie #{gameState.id}
                    </h2>
                    <p className="text-xs text-muted-foreground">
                      Position: {gameState.playerPosition} / {gameState.board.cells.length - 1}
                    </p>
                    
                    <div className="flex flex-col gap-2">
                      <div className="flex flex-col">
                        <Button
                          variant="outline"
                          size="sm"
                          onClick={refreshGameState}
                          disabled={isRefreshing}
                          className="w-full"
                        >
                          {isRefreshing ? (
                            <Loader2 className="w-3 h-3 animate-spin mr-2" />
                          ) : (
                            <RotateCcw className="w-3 h-3 mr-2" />
                          )}
                          Actualiser
                        </Button>
                        <span className="text-xs text-muted-foreground mt-1 text-center opacity-70">
                          GET /games/{gameState.id}
                        </span>
                      </div>
                      
                      <Button
                        variant="destructive"
                        size="sm"
                        onClick={resetGame}
                        className="w-full"
                      >
                        Nouvelle partie
                      </Button>
                    </div>
                  </div>
                </div>

                {/* Player Stats */}
                <PlayerStats player={gameState.player} />
              </div>

              {/* Middle Column - Game Board and Logs */}
              <div className="lg:col-span-3 flex flex-col space-y-4 min-h-0">
                {/* Game Board */}
                <div className="flex-shrink-0">
                  <GameBoard 
                    board={gameState.board.cells}
                    currentPosition={gameState.playerPosition}
                  />
                </div>

                {/* Game Logs - Takes remaining space */}
                <div className="flex-1 min-h-0">
                  <GameLogs
                    logs={gameLogs}
                    isLoading={isLogsLoading}
                    gameId={gameState.id}
                  />
                </div>
              </div>

              {/* Right Column - Actions */}
              <div className="lg:col-span-1 flex-shrink-0">
                <ActionPanel
                  onMove={handleMove}
                  onInteract={handleInteract}
                  isLoading={isActionLoading}
                  isGameOver={isGameOver(gameState)}
                  gameWon={isGameWon(gameState)}
                  gameId={gameState.id}
                />
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default Game;