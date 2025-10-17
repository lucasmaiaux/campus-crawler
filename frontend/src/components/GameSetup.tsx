import React, { useState } from 'react';
import { Card } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from '@/components/ui/dialog';
import { CreateGameRequest, GameState } from '@/types/game';
import { gameApi, ApiError } from '@/services/gameApi';
import { toast } from '@/hooks/use-toast';
import { Loader2, Play, Upload } from 'lucide-react';
import { GamesList } from './GamesList';
import { PlayerBoardSelection } from './PlayerBoardSelection';

interface GameSetupProps {
  onGameCreated: (gameState: GameState) => void;
  isLoading: boolean;
}

export const GameSetup: React.FC<GameSetupProps> = ({ onGameCreated, isLoading }) => {
  const [loadGameId, setLoadGameId] = useState<string>('');
  const [isCreating, setIsCreating] = useState(false);
  const [isLoading_, setIsLoading_] = useState(false);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [showPlayerBoardSelection, setShowPlayerBoardSelection] = useState(false);

  const handleCreateGame = async (playerId: number, boardId: number) => {
    setIsCreating(true);
    try {
      const request: CreateGameRequest = {
        playerId,
        boardId,
      };
      
      const gameState = await gameApi.createGame(request);
      onGameCreated(gameState);
      
      toast({
        title: "Partie créée",
        description: `Nouvelle partie démarrée (ID: ${gameState.id})`,
      });
    } catch (error) {
      const apiError = error as ApiError;
      toast({
        title: "Erreur",
        description: apiError.message || "Impossible de créer la partie",
        variant: "destructive",
      });
    } finally {
      setIsCreating(false);
      setShowPlayerBoardSelection(false);
    }
  };

  const handleLoadGame = async (gameId: string) => {
    if (!gameId) {
      toast({
        title: "Erreur",
        description: "Veuillez saisir un ID de partie valide",
        variant: "destructive",
      });
      return;
    }

    setIsLoading_(true);
    try {
      const gameState = await gameApi.getGameState(parseInt(gameId));
      onGameCreated(gameState);
      setIsDialogOpen(false);
      
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
      setIsLoading_(false);
    }
  };

  const isFormDisabled = isLoading || isCreating;

  if (showPlayerBoardSelection) {
    return (
      <div className="w-full">
        <PlayerBoardSelection
          onSelectionComplete={handleCreateGame}
          onCancel={() => setShowPlayerBoardSelection(false)}
        />
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <Card className="bg-gradient-card shadow-card border-border">
        <div className="p-6 space-y-6">
          <div className="text-center">
            <h2 className="text-2xl font-bold bg-gradient-primary bg-clip-text text-transparent mb-2">
              Configuration de partie
            </h2>
            <p className="text-muted-foreground">Créez une nouvelle partie ou chargez une partie existante</p>
          </div>

        <div className="flex flex-col sm:flex-row gap-3">
          <div className="flex-1 flex flex-col">
            <Button
              onClick={() => setShowPlayerBoardSelection(true)}
              disabled={isFormDisabled}
              variant="destructive"
            >
              {isCreating ? <Loader2 className="w-4 h-4 animate-spin mr-2" /> : <Play className="w-4 h-4 mr-2" />}
              Nouvelle partie
            </Button>
            <span className="text-xs text-muted-foreground mt-1 text-center opacity-70">
              POST /games
            </span>
          </div>

          <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
            <DialogTrigger asChild>
              <div className="flex-1 flex flex-col">
                <Button variant="secondary" disabled={isFormDisabled}>
                  <Upload className="w-4 h-4 mr-2" />
                  Continuer partie
                </Button>
                <span className="text-xs text-muted-foreground mt-1 text-center opacity-70">
                  GET /games/{'{id}'}
                </span>
              </div>
            </DialogTrigger>
            <DialogContent className="bg-card border-border">
              <DialogHeader>
                <DialogTitle>Charger une partie</DialogTitle>
              </DialogHeader>
              <div className="space-y-4">
                <div className="space-y-2">
                  <Label htmlFor="loadGameId">Game ID</Label>
                  <Input
                    id="loadGameId"
                    type="number"
                    value={loadGameId}
                    onChange={(e) => setLoadGameId(e.target.value)}
                    placeholder="Entrez l'ID de la partie"
                    className="bg-input"
                  />
                </div>
                <div className="flex flex-col">
                  <Button
                    onClick={() => handleLoadGame(loadGameId)}
                    disabled={isLoading_}
                    variant="destructive"
                  >
                    {isLoading_ ? <Loader2 className="w-4 h-4 animate-spin mr-2" /> : null}
                    Charger
                  </Button>
                  <span className="text-xs text-muted-foreground mt-1 text-center opacity-70">
                    GET /games/{loadGameId || '{id}'}
                  </span>
                </div>
              </div>
            </DialogContent>
          </Dialog>
        </div>
      </div>
    </Card>

    <GamesList onGameSelected={onGameCreated} />
    </div>
  );
};