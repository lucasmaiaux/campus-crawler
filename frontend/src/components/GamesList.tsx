import React, { useState, useEffect } from 'react';
import { Card } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { GameState, isGameOver, isGameWon } from '@/types/game';
import { gameApi, ApiError } from '@/services/gameApi';
import { toast } from '@/hooks/use-toast';
import { Loader2, Play, RefreshCw, Trash2 } from 'lucide-react';

interface GamesListProps {
  onGameSelected: (gameState: GameState) => void;
}

export const GamesList: React.FC<GamesListProps> = ({ onGameSelected }) => {
  const [games, setGames] = useState<GameState[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [loadingGameId, setLoadingGameId] = useState<number | null>(null);
  const [deletingGameId, setDeletingGameId] = useState<number | null>(null);

  const loadGames = async () => {
    setIsLoading(true);
    try {
      const gamesList = await gameApi.getAllGames();
      setGames(gamesList);
    } catch (error) {
      const apiError = error as ApiError;
      toast({
        title: "Erreur",
        description: apiError.message || "Impossible de charger les parties",
        variant: "destructive",
      });
    } finally {
      setIsLoading(false);
    }
  };

  const handleSelectGame = async (gameId: number) => {
    setLoadingGameId(gameId);
    try {
      const gameState = await gameApi.getGameState(gameId);
      onGameSelected(gameState);
      
      toast({
        title: "Partie chargée",
        description: `Partie ${gameId} chargée avec succès`,
      });
    } catch (error) {
      const apiError = error as ApiError;
      toast({
        title: "Erreur",
        description: apiError.message || "Impossible de charger la partie",
        variant: "destructive",
      });
    } finally {
      setLoadingGameId(null);
    }
  };

  const handleDeleteGame = async (gameId: number) => {
    // if (!window.confirm(`Êtes-vous sûr de vouloir supprimer la partie #${gameId} ? Cette action est irréversible.`)) {
    //   return;
    // }

    setDeletingGameId(gameId);
    try {
      await gameApi.deleteGame(gameId);
      setGames(games.filter(game => game.id !== gameId));
      
      toast({
        title: "Partie supprimée",
        description: `Partie ${gameId} supprimée avec succès`,
      });
    } catch (error) {
      const apiError = error as ApiError;
      toast({
        title: "Erreur de suppression",
        description: apiError.message || "Impossible de supprimer la partie",
        variant: "destructive",
      });
    } finally {
      setDeletingGameId(null);
    }
  };

  useEffect(() => {
    loadGames();
  }, []);

  return (
    <Card className="bg-gradient-card shadow-card border-border">
      <div className="p-6">
        <div className="flex items-center justify-between mb-4">
          <div className="flex items-center gap-3">
            <h3 className="text-xl font-semibold text-foreground">Parties existantes</h3>
            <span className="text-xs text-muted-foreground opacity-70">
              GET /games
            </span>
          </div>
          <Button
            variant="outline"
            size="sm"
            onClick={loadGames}
            disabled={isLoading}
          >
            {isLoading ? (
              <Loader2 className="w-4 h-4 animate-spin" />
            ) : (
              <RefreshCw className="w-4 h-4" />
            )}
          </Button>
        </div>

        {isLoading ? (
          <div className="flex items-center justify-center py-8">
            <Loader2 className="w-6 h-6 animate-spin mr-2" />
            <span className="text-muted-foreground">Chargement des parties...</span>
          </div>
        ) : games.length === 0 ? (
          <div className="text-center py-8">
            <p className="text-muted-foreground">Aucune partie trouvée</p>
          </div>
        ) : (
          <div className="space-y-3 max-h-96 overflow-y-auto">
            {games.map((game) => (
              <div
                key={game.id}
                className="flex items-center justify-between p-3 bg-card/50 rounded-lg border border-border/50 hover:bg-card/80 transition-colors"
              >
                <div className="space-y-1 flex-1">
                  <div className="flex items-center gap-2">
                    <span className="font-medium text-foreground">
                      Partie #{game.id}
                    </span>
                    {isGameOver(game) && (
                      <Badge
                        variant={isGameWon(game) ? "default" : "destructive"}
                        className={`text-xs text-white ${isGameWon(game) ? 'bg-green-600 hover:bg-green-700' : 'bg-red-600 hover:bg-red-700'}`}
                      >
                        {isGameWon(game) ? "Gagnée" : "Perdue"}
                      </Badge>
                    )}
                    {!isGameOver(game) && (
                      <Badge variant="secondary" className="text-xs">
                        En cours
                      </Badge>
                    )}
                  </div>
                  <div className="text-sm text-muted-foreground space-y-0.5">
                    <div className="flex gap-4">
                      <span>Board : {game.board?.id || 'N/A'}</span>
                      <span>Joueur : {game.player?.name || 'N/A'}</span>
                    </div>
                    <div className="flex gap-4">
                      <span>Position : {game.playerPosition}/{(game.board?.cells?.length || 1) - 1}</span>
                      <span>PV : {game.player?.health || 0}/{game.player?.maxHealth || 0}</span>
                    </div>
                  </div>
                </div>
                <div className="flex flex-col gap-2">
                  <div className="flex gap-2">
                    <Button
                      size="sm"
                      onClick={() => handleSelectGame(game.id)}
                      disabled={loadingGameId === game.id || deletingGameId === game.id}
                      className="bg-gradient-primary hover:opacity-90 transition-opacity"
                    >
                      {loadingGameId === game.id ? (
                        <Loader2 className="w-3 h-3 animate-spin mr-1" />
                      ) : (
                        <Play className="w-3 h-3 mr-1" />
                      )}
                      Jouer
                    </Button>
                    <Button
                      size="sm"
                      variant="destructive"
                      onClick={() => handleDeleteGame(game.id)}
                      disabled={loadingGameId === game.id || deletingGameId === game.id}
                      className="px-2"
                    >
                      {deletingGameId === game.id ? (
                        <Loader2 className="w-3 h-3 animate-spin" />
                      ) : (
                        <Trash2 className="w-3 h-3" />
                      )}
                    </Button>
                  </div>
                  <div className="flex flex-col text-center">
                    <span className="text-xs text-muted-foreground opacity-70">
                      GET /games/{game.id}
                    </span>
                    <span className="text-xs text-muted-foreground opacity-70">
                      DELETE /games/{game.id}
                    </span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </Card>
  );
};