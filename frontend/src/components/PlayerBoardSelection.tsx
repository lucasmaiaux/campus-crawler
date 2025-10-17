import React, { useState, useEffect } from 'react';
import { Card } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from '@/components/ui/dialog';
import { Player, Board, PlayerNameDTO, BoardDTO } from '@/types/game';
import { gameApi, ApiError } from '@/services/gameApi';
import { toast } from '@/hooks/use-toast';
import { Loader2, Plus, User, Map, RefreshCw, Trash2, X, Sword, Zap } from 'lucide-react';

interface PlayerBoardSelectionProps {
  onSelectionComplete: (playerId: number, boardId: number) => void;
  onCancel: () => void;
}

export const PlayerBoardSelection: React.FC<PlayerBoardSelectionProps> = ({ onSelectionComplete, onCancel }) => {
  const [players, setPlayers] = useState<Player[]>([]);
  const [boards, setBoards] = useState<Board[]>([]);
  const [selectedPlayerId, setSelectedPlayerId] = useState<number | null>(null);
  const [selectedBoardId, setSelectedBoardId] = useState<number | null>(null);
  const [isLoadingPlayers, setIsLoadingPlayers] = useState(false);
  const [isLoadingBoards, setIsLoadingBoards] = useState(false);
  const [isCreatingPlayer, setIsCreatingPlayer] = useState(false);
  const [isCreatingBoard, setIsCreatingBoard] = useState(false);
  const [playerDialogOpen, setPlayerDialogOpen] = useState(false);
  const [boardDialogOpen, setBoardDialogOpen] = useState(false);
  const [deletingPlayerId, setDeletingPlayerId] = useState<number | null>(null);
  const [deletingBoardId, setDeletingBoardId] = useState<number | null>(null);

  // Player creation form
  const [newPlayerName, setNewPlayerName] = useState('');
  const [newPlayerType, setNewPlayerType] = useState<'Warrior' | 'Wizard'>('Warrior');
  
  // Board creation form
  const [boardConfig, setBoardConfig] = useState<BoardDTO>({
    boardSize: 32,
    cellsEnemyDragon: 2,
    cellsEnemyWitch: 3,
    cellsEnemyGoblin: 3,
    cellsEnemyOrc: 3,
    cellsWeaponHammer: 1,
    cellsWeaponSword: 1,
    cellsWeaponAxe: 1,
    cellsWeaponLegendary: 1,
    cellsSpellThunderstorm: 1,
    cellsSpellFireball: 1,
    cellsSpellMeteor: 1,
    cellsArmor: 1,
    cellsHelmet: 1,
    cellsShield: 1
  });

  const [boardPreset, setBoardPreset] = useState<'Petit' | 'Moyen' | 'Grand'>('Petit');

  // Board presets
  const boardPresets = {
    'Petit': {
      boardSize: 32,
      cellsEnemyDragon: 2,
      cellsEnemyWitch: 3,
      cellsEnemyGoblin: 3,
      cellsEnemyOrc: 3,
      cellsWeaponHammer: 1,
      cellsWeaponSword: 1,
      cellsWeaponAxe: 1,
      cellsWeaponLegendary: 1,
      cellsSpellThunderstorm: 1,
      cellsSpellFireball: 1,
      cellsSpellMeteor: 1,
      cellsArmor: 1,
      cellsHelmet: 1,
      cellsShield: 1
    },
    'Moyen': {
      boardSize: 64,
      cellsEnemyDragon: 4,
      cellsEnemyWitch: 6,
      cellsEnemyGoblin: 6,
      cellsEnemyOrc: 6,
      cellsWeaponHammer: 2,
      cellsWeaponSword: 2,
      cellsWeaponAxe: 2,
      cellsWeaponLegendary: 2,
      cellsSpellThunderstorm: 2,
      cellsSpellFireball: 2,
      cellsSpellMeteor: 2,
      cellsArmor: 2,
      cellsHelmet: 2,
      cellsShield: 2
    },
    'Grand': {
      boardSize: 128,
      cellsEnemyDragon: 8,
      cellsEnemyWitch: 12,
      cellsEnemyGoblin: 12,
      cellsEnemyOrc: 12,
      cellsWeaponHammer: 4,
      cellsWeaponSword: 4,
      cellsWeaponAxe: 4,
      cellsWeaponLegendary: 4,
      cellsSpellThunderstorm: 4,
      cellsSpellFireball: 4,
      cellsSpellMeteor: 4,
      cellsArmor: 4,
      cellsHelmet: 4,
      cellsShield: 4
    }
  };

  const handlePresetChange = (preset: 'Petit' | 'Moyen' | 'Grand') => {
    setBoardPreset(preset);
    setBoardConfig(boardPresets[preset]);
  };

  const loadPlayers = async () => {
    setIsLoadingPlayers(true);
    try {
      const playersData = await gameApi.getPlayers();
      setPlayers(playersData);
    } catch (error) {
      const apiError = error as ApiError;
      toast({
        title: "Erreur",
        description: apiError.message || "Impossible de charger les joueurs",
        variant: "destructive",
      });
    } finally {
      setIsLoadingPlayers(false);
    }
  };

  const loadBoards = async () => {
    setIsLoadingBoards(true);
    try {
      const boardsData = await gameApi.getBoards();
      setBoards(boardsData);
    } catch (error) {
      const apiError = error as ApiError;
      toast({
        title: "Erreur",
        description: apiError.message || "Impossible de charger les plateaux",
        variant: "destructive",
      });
    } finally {
      setIsLoadingBoards(false);
    }
  };

  const handleCreatePlayer = async () => {
    if (!newPlayerName.trim()) {
      toast({
        title: "Erreur",
        description: "Veuillez saisir un nom pour le joueur",
        variant: "destructive",
      });
      return;
    }

    setIsCreatingPlayer(true);
    try {
      const playerNameDTO: PlayerNameDTO = {
        type: newPlayerType,
        name: newPlayerName,
      };
      
      const newPlayer = await gameApi.createPlayer(playerNameDTO);
      setPlayers([...players, newPlayer]);
      setSelectedPlayerId(newPlayer.id);
      setPlayerDialogOpen(false);
      setNewPlayerName('');
      
      toast({
        title: "Joueur créé",
        description: `${newPlayer.name} (${newPlayer.type}) a été créé avec succès`,
      });
    } catch (error) {
      const apiError = error as ApiError;
      toast({
        title: "Erreur",
        description: apiError.message || "Impossible de créer le joueur",
        variant: "destructive",
      });
    } finally {
      setIsCreatingPlayer(false);
    }
  };

  const handleCreateBoard = async () => {
    setIsCreatingBoard(true);
    try {
      const newBoard = await gameApi.createBoard(boardConfig);
      setBoards([...boards, newBoard]);
      setSelectedBoardId(newBoard.id);
      setBoardDialogOpen(false);
      
      toast({
        title: "Plateau créé",
        description: `Nouveau plateau de ${newBoard.boardSize} cases créé avec succès`,
      });
    } catch (error) {
      const apiError = error as ApiError;
      toast({
        title: "Erreur",
        description: apiError.message || "Impossible de créer le plateau",
        variant: "destructive",
      });
    } finally {
      setIsCreatingBoard(false);
    }
  };

  const handleDeletePlayer = async (playerId: number) => {
    // if (!window.confirm(`Êtes-vous sûr de vouloir supprimer ce joueur ? Cette action est irréversible.`)) {
    //   return;
    // }

    setDeletingPlayerId(playerId);
    try {
      await gameApi.deletePlayer(playerId);
      setPlayers(players.filter(player => player.id !== playerId));
      if (selectedPlayerId === playerId) {
        setSelectedPlayerId(null);
      }
      
      toast({
        title: "Joueur supprimé",
        description: `Le joueur a été supprimé avec succès`,
      });
    } catch (error) {
      const apiError = error as ApiError;
      toast({
        title: "Erreur de suppression",
        description: apiError.message || "Impossible de supprimer le joueur",
        variant: "destructive",
      });
    } finally {
      setDeletingPlayerId(null);
    }
  };

  const handleDeleteBoard = async (boardId: number) => {
    // if (!window.confirm(`Êtes-vous sûr de vouloir supprimer ce plateau ? Cette action est irréversible.`)) {
    //   return;
    // }

    setDeletingBoardId(boardId);
    try {
      await gameApi.deleteBoard(boardId);
      setBoards(boards.filter(board => board.id !== boardId));
      if (selectedBoardId === boardId) {
        setSelectedBoardId(null);
      }
      
      toast({
        title: "Plateau supprimé",
        description: `Le plateau a été supprimé avec succès`,
      });
    } catch (error) {
      const apiError = error as ApiError;
      toast({
        title: "Erreur de suppression",
        description: apiError.message || "Impossible de supprimer le plateau",
        variant: "destructive",
      });
    } finally {
      setDeletingBoardId(null);
    }
  };

  const handleConfirm = () => {
    if (selectedPlayerId && selectedBoardId) {
      // Check if selected player is dead
      const selectedPlayer = players.find(player => player.id === selectedPlayerId);
      if (selectedPlayer && selectedPlayer.health <= 0) {
        toast({
          title: "Erreur",
          description: "Impossible de créer une partie avec un joueur décédé. Veuillez sélectionner un joueur avec des PV supérieurs à 0.",
          variant: "destructive",
        });
        return;
      }

      onSelectionComplete(selectedPlayerId, selectedBoardId);
    }
  };

  useEffect(() => {
    loadPlayers();
    loadBoards();
  }, []);

  return (
    <div className="space-y-6 max-w-6xl mx-auto px-4">
      <Card className="bg-gradient-card shadow-card border-border relative">
        {/* Close button */}
        <Button
          variant="destructive"
          size="sm"
          onClick={onCancel}
          className="absolute -top-4 -right-4 h-10 w-10 p-0 rounded-full border-2 border-border bg-destructive hover:bg-destructive/90 z-10"
        >
          <X className="h-7 w-7" />
        </Button>
        <div className="p-8">

          <div className="text-center mb-6">
            <h2 className="text-2xl font-bold bg-gradient-primary bg-clip-text text-transparent mb-2">
              Création d'une nouvelle partie
            </h2>
            <p className="text-muted-foreground">Choisissez votre joueur et plateau pour commencer l'aventure</p>
          </div>

          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            {/* Players Column */}
            <div className="space-y-4">
              <div className="flex items-center justify-between">
                <h3 className="text-lg font-semibold flex items-center gap-2">
                  <User className="w-5 h-5" />
                  Joueurs
                </h3>
                <Button
                  variant="outline"
                  size="sm"
                  onClick={loadPlayers}
                  disabled={isLoadingPlayers}
                >
                  {isLoadingPlayers ? (
                    <Loader2 className="w-4 h-4 animate-spin" />
                  ) : (
                    <RefreshCw className="w-4 h-4" />
                  )}
                </Button>
              </div>
              <span className="text-xs text-muted-foreground opacity-70">GET /players</span>

              {isLoadingPlayers ? (
                <div className="flex items-center justify-center py-8">
                  <Loader2 className="w-6 h-6 animate-spin mr-2" />
                  <span className="text-muted-foreground">Chargement...</span>
                </div>
              ) : (
                <div className="space-y-2 max-h-80 overflow-y-auto">
                  {players && players.length > 0 ? players.map((player) => (
                    <div
                      key={player.id}
                      className={`p-3 rounded-lg border transition-colors ${
                        selectedPlayerId === player.id
                          ? 'bg-primary/20 border-primary'
                          : 'bg-card/50 border-border/50 hover:bg-card/80'
                      }`}
                    >
                      <div className="flex items-center justify-between">
                        <div
                          className="flex-1 cursor-pointer"
                          onClick={() => setSelectedPlayerId(selectedPlayerId === player.id ? null : player.id)}
                        >
                          <div className="flex items-center gap-2">
                            <span className="font-medium">{player.name}</span>
                            <Badge
                              variant="secondary"
                              className={player.type === 'Warrior' ? 'bg-destructive/20 text-destructive' : 'bg-cell-spell/20 text-purple-300'}
                            >
                              {player.type}
                            </Badge>
                            {player.health <= 0 && (
                              <span className="text-base">☠️</span>
                            )}
                          </div>
                          <div className="text-sm text-muted-foreground">
                            PV: {player.health}/{player.maxHealth} • Attaque: {player.base_attack + (player.offensiveEquipment?.attack || 0)} • Défense: {player.base_defense + (player.defensiveEquipmentHelmet?.defense || 0) + (player.defensiveEquipmentArmor?.defense || 0) + (player.defensiveEquipmentShield?.defense || 0)}
                          </div>
                        </div>
                        <div className="flex flex-col items-center gap-1 ml-2">
                          <Button
                            size="sm"
                            variant="destructive"
                            onClick={() => handleDeletePlayer(player.id)}
                            disabled={deletingPlayerId === player.id || isCreatingPlayer}
                            className="px-2 h-8"
                          >
                            {deletingPlayerId === player.id ? (
                              <Loader2 className="w-3 h-3 animate-spin" />
                            ) : (
                              <Trash2 className="w-3 h-3" />
                            )}
                          </Button>
                          <span className="text-xs text-muted-foreground opacity-70">
                            DELETE /players/{player.id}
                          </span>
                        </div>
                      </div>
                    </div>
                  )) : (
                    <div className="text-center py-8">
                      <p className="text-muted-foreground">Aucun joueur trouvé</p>
                    </div>
                  )}
                </div>
              )}

              <Dialog open={playerDialogOpen} onOpenChange={setPlayerDialogOpen}>
                <DialogTrigger asChild>
                  <div className="flex flex-col">
                    <Button variant="outline" className="w-full">
                      <Plus className="w-4 h-4 mr-2" />
                      Créer un nouveau joueur
                    </Button>
                    <span className="text-xs text-muted-foreground mt-1 text-center opacity-70">
                      POST /players/name
                    </span>
                  </div>
                </DialogTrigger>
                <DialogContent>
                  <DialogHeader>
                    <DialogTitle>Créer un nouveau joueur</DialogTitle>
                  </DialogHeader>
                  <div className="space-y-4">
                    <div>
                      <Label>Nom du joueur</Label>
                      <Input
                        value={newPlayerName}
                        onChange={(e) => setNewPlayerName(e.target.value)}
                        placeholder="Nom du personnage"
                      />
                    </div>
                    <div>
                      <Label>Type de joueur</Label>
                      <div className="flex gap-2 mt-2">
                        <Button
                          variant={newPlayerType === 'Warrior' ? 'default' : 'outline'}
                          size="sm"
                          onClick={() => setNewPlayerType('Warrior')}
                          className={newPlayerType === 'Warrior' ? 'bg-destructive/20 text-destructive border-destructive/30 hover:bg-destructive/30' : 'hover:bg-destructive/20 hover:text-destructive hover:border-destructive/30'}
                        >
                          <Sword className="w-3 h-3 mr-1" />
                          Guerrier
                        </Button>
                        <Button
                          variant={newPlayerType === 'Wizard' ? 'default' : 'outline'}
                          size="sm"
                          onClick={() => setNewPlayerType('Wizard')}
                          className={newPlayerType === 'Wizard' ? 'bg-cell-spell/20 text-purple-300 border-purple-300/30 hover:bg-cell-spell/30' : 'hover:bg-cell-spell/20 hover:text-purple-300 hover:border-purple-300/30'}
                        >
                          <Zap className="w-3 h-3 mr-1" />
                          Magicien
                        </Button>
                      </div>
                    </div>
                    <div className="flex flex-col">
                      <Button onClick={handleCreatePlayer} disabled={isCreatingPlayer}>
                        {isCreatingPlayer && <Loader2 className="w-4 h-4 animate-spin mr-2" />}
                        Créer le joueur
                      </Button>
                      <span className="text-xs text-muted-foreground mt-1 text-center opacity-70">
                        POST /players/name
                      </span>
                    </div>
                  </div>
                </DialogContent>
              </Dialog>
            </div>

            {/* Boards Column */}
            <div className="space-y-4">
              <div className="flex items-center justify-between">
                <h3 className="text-lg font-semibold flex items-center gap-2">
                  <Map className="w-5 h-5" />
                  Plateaux
                </h3>
                <Button
                  variant="outline"
                  size="sm"
                  onClick={loadBoards}
                  disabled={isLoadingBoards}
                >
                  {isLoadingBoards ? (
                    <Loader2 className="w-4 h-4 animate-spin" />
                  ) : (
                    <RefreshCw className="w-4 h-4" />
                  )}
                </Button>
              </div>
              <span className="text-xs text-muted-foreground opacity-70">GET /boards</span>

              {isLoadingBoards ? (
                <div className="flex items-center justify-center py-8">
                  <Loader2 className="w-6 h-6 animate-spin mr-2" />
                  <span className="text-muted-foreground">Chargement...</span>
                </div>
              ) : (
                <div className="space-y-2 max-h-80 overflow-y-auto">
                  {boards && boards.length > 0 ? boards.map((board) => (
                    <div
                      key={board.id}
                      className={`p-3 rounded-lg border transition-colors ${
                        selectedBoardId === board.id
                          ? 'bg-primary/20 border-primary'
                          : 'bg-card/50 border-border/50 hover:bg-card/80'
                      }`}
                    >
                      <div className="flex items-center justify-between">
                        <div
                          className="flex-1 cursor-pointer"
                          onClick={() => setSelectedBoardId(selectedBoardId === board.id ? null : board.id)}
                        >
                          <div className="flex items-center gap-2">
                            <span className="font-medium">Plateau #{board.id}</span>
                          </div>
                          <div className="text-sm text-muted-foreground">
                            {board.cells?.length || 0} cases
                          </div>
                        </div>
                        <div className="flex flex-col items-center gap-1 ml-2">
                          <Button
                            size="sm"
                            variant="destructive"
                            onClick={() => handleDeleteBoard(board.id)}
                            disabled={deletingBoardId === board.id || isCreatingBoard}
                            className="px-2 h-8"
                          >
                            {deletingBoardId === board.id ? (
                              <Loader2 className="w-3 h-3 animate-spin" />
                            ) : (
                              <Trash2 className="w-3 h-3" />
                            )}
                          </Button>
                          <span className="text-xs text-muted-foreground opacity-70">
                            DELETE /boards/{board.id}
                          </span>
                        </div>
                      </div>
                    </div>
                  )) : (
                    <div className="text-center py-8">
                      <p className="text-muted-foreground">Aucun plateau trouvé</p>
                    </div>
                  )}
                </div>
              )}

              <Dialog open={boardDialogOpen} onOpenChange={setBoardDialogOpen}>
                <DialogTrigger asChild>
                  <div className="flex flex-col">
                    <Button variant="outline" className="w-full">
                      <Plus className="w-4 h-4 mr-2" />
                      Créer un nouveau plateau
                    </Button>
                    <span className="text-xs text-muted-foreground mt-1 text-center opacity-70">
                      POST /boards
                    </span>
                  </div>
                </DialogTrigger>
                <DialogContent className="max-w-6xl max-h-[80vh] overflow-y-auto">
                  <DialogHeader>
                    <DialogTitle>Créer un nouveau plateau</DialogTitle>
                  </DialogHeader>
                  <div className="space-y-4">
                    <div>
                      <Label>Presets de plateau</Label>
                      <div className="flex gap-2 mt-2">
                        <Button
                          variant={boardPreset === 'Petit' ? 'default' : 'outline'}
                          size="sm"
                          onClick={() => handlePresetChange('Petit')}
                          className={boardPreset === 'Petit' ? 'bg-green-600/20 text-green-300 border-green-300/30 hover:bg-green-600/30' : 'hover:bg-green-600/20 hover:text-green-300 hover:border-green-300/30'}
                        >
                          Petit
                        </Button>
                        <Button
                          variant={boardPreset === 'Moyen' ? 'default' : 'outline'}
                          size="sm"
                          onClick={() => handlePresetChange('Moyen')}
                          className={boardPreset === 'Moyen' ? 'bg-yellow-600/20 text-yellow-300 border-yellow-300/30 hover:bg-yellow-600/30' : 'hover:bg-yellow-600/20 hover:text-yellow-300 hover:border-yellow-300/30'}
                        >
                          Moyen
                        </Button>
                        <Button
                          variant={boardPreset === 'Grand' ? 'default' : 'outline'}
                          size="sm"
                          onClick={() => handlePresetChange('Grand')}
                          className={boardPreset === 'Grand' ? 'bg-red-600/20 text-red-300 border-red-300/30 hover:bg-red-600/30' : 'hover:bg-red-600/20 hover:text-red-300 hover:border-red-300/30'}
                        >
                          Grand
                        </Button>
                      </div>
                    </div>

                    <div>
                      <Label>Taille du plateau</Label>
                      <Input
                        type="number"
                        value={boardConfig.boardSize}
                        onChange={(e) => setBoardConfig({
                          ...boardConfig,
                          boardSize: parseInt(e.target.value) || 10
                        })}
                        min={5}
                        max={200}
                        className="max-w-24"
                      />
                    </div>
                    
                    <div className="grid grid-cols-3 gap-4">
                      <div>
                        <Label className="text-sm font-medium">Ennemis</Label>
                        <div className="space-y-2 mt-2">
                          <div className="flex items-center justify-between">
                            <span className="text-sm">Dragon</span>
                            <Input
                              type="number"
                              className="w-16 h-8"
                              min={0}
                              value={boardConfig.cellsEnemyDragon}
                              onChange={(e) => setBoardConfig({
                                ...boardConfig,
                                cellsEnemyDragon: parseInt(e.target.value) || 0
                              })}
                            />
                          </div>
                          <div className="flex items-center justify-between">
                            <span className="text-sm">Witch</span>
                            <Input
                              type="number"
                              className="w-16 h-8"
                              min={0}
                              value={boardConfig.cellsEnemyWitch}
                              onChange={(e) => setBoardConfig({
                                ...boardConfig,
                                cellsEnemyWitch: parseInt(e.target.value) || 0
                              })}
                            />
                          </div>
                          <div className="flex items-center justify-between">
                            <span className="text-sm">Goblin</span>
                            <Input
                              type="number"
                              className="w-16 h-8"
                              min={0}
                              value={boardConfig.cellsEnemyGoblin}
                              onChange={(e) => setBoardConfig({
                                ...boardConfig,
                                cellsEnemyGoblin: parseInt(e.target.value) || 0
                              })}
                            />
                          </div>
                          <div className="flex items-center justify-between">
                            <span className="text-sm">Orc</span>
                            <Input
                              type="number"
                              className="w-16 h-8"
                              min={0}
                              value={boardConfig.cellsEnemyOrc}
                              onChange={(e) => setBoardConfig({
                                ...boardConfig,
                                cellsEnemyOrc: parseInt(e.target.value) || 0
                              })}
                            />
                          </div>
                        </div>
                      </div>
                      
                      <div>
                        <Label className="text-sm font-medium">Armes</Label>
                        <div className="space-y-2 mt-2">
                          <div className="flex items-center justify-between">
                            <span className="text-sm">Hammer</span>
                            <Input
                              type="number"
                              className="w-16 h-8"
                              min={0}
                              value={boardConfig.cellsWeaponHammer}
                              onChange={(e) => setBoardConfig({
                                ...boardConfig,
                                cellsWeaponHammer: parseInt(e.target.value) || 0
                              })}
                            />
                          </div>
                          <div className="flex items-center justify-between">
                            <span className="text-sm">Sword</span>
                            <Input
                              type="number"
                              className="w-16 h-8"
                              min={0}
                              value={boardConfig.cellsWeaponSword}
                              onChange={(e) => setBoardConfig({
                                ...boardConfig,
                                cellsWeaponSword: parseInt(e.target.value) || 0
                              })}
                            />
                          </div>
                          <div className="flex items-center justify-between">
                            <span className="text-sm">Axe</span>
                            <Input
                              type="number"
                              className="w-16 h-8"
                              min={0}
                              value={boardConfig.cellsWeaponAxe}
                              onChange={(e) => setBoardConfig({
                                ...boardConfig,
                                cellsWeaponAxe: parseInt(e.target.value) || 0
                              })}
                            />
                          </div>
                          <div className="flex items-center justify-between">
                            <span className="text-sm">Legendary</span>
                            <Input
                              type="number"
                              className="w-16 h-8"
                              min={0}
                              value={boardConfig.cellsWeaponLegendary}
                              onChange={(e) => setBoardConfig({
                                ...boardConfig,
                                cellsWeaponLegendary: parseInt(e.target.value) || 0
                              })}
                            />
                          </div>
                        </div>
                      </div>

                      <div>
                        <Label className="text-sm font-medium">Sorts & Défense</Label>
                        <div className="space-y-2 mt-2">
                          <div className="flex items-center justify-between">
                            <span className="text-sm">Thunderstorm</span>
                            <Input
                              type="number"
                              className="w-16 h-8"
                              min={0}
                              value={boardConfig.cellsSpellThunderstorm}
                              onChange={(e) => setBoardConfig({
                                ...boardConfig,
                                cellsSpellThunderstorm: parseInt(e.target.value) || 0
                              })}
                            />
                          </div>
                          <div className="flex items-center justify-between">
                            <span className="text-sm">Fireball</span>
                            <Input
                              type="number"
                              className="w-16 h-8"
                              min={0}
                              value={boardConfig.cellsSpellFireball}
                              onChange={(e) => setBoardConfig({
                                ...boardConfig,
                                cellsSpellFireball: parseInt(e.target.value) || 0
                              })}
                            />
                          </div>
                          <div className="flex items-center justify-between">
                            <span className="text-sm">Meteor</span>
                            <Input
                              type="number"
                              className="w-16 h-8"
                              min={0}
                              value={boardConfig.cellsSpellMeteor}
                              onChange={(e) => setBoardConfig({
                                ...boardConfig,
                                cellsSpellMeteor: parseInt(e.target.value) || 0
                              })}
                            />
                          </div>
                          <div className="flex items-center justify-between">
                            <span className="text-sm">Armor</span>
                            <Input
                              type="number"
                              className="w-16 h-8"
                              min={0}
                              value={boardConfig.cellsArmor}
                              onChange={(e) => setBoardConfig({
                                ...boardConfig,
                                cellsArmor: parseInt(e.target.value) || 0
                              })}
                            />
                          </div>
                          <div className="flex items-center justify-between">
                            <span className="text-sm">Helmet</span>
                            <Input
                              type="number"
                              className="w-16 h-8"
                              min={0}
                              value={boardConfig.cellsHelmet}
                              onChange={(e) => setBoardConfig({
                                ...boardConfig,
                                cellsHelmet: parseInt(e.target.value) || 0
                              })}
                            />
                          </div>
                          <div className="flex items-center justify-between">
                            <span className="text-sm">Shield</span>
                            <Input
                              type="number"
                              className="w-16 h-8"
                              min={0}
                              value={boardConfig.cellsShield}
                              onChange={(e) => setBoardConfig({
                                ...boardConfig,
                                cellsShield: parseInt(e.target.value) || 0
                              })}
                            />
                          </div>
                        </div>
                      </div>
                    </div>
                    
                    <div className="flex flex-col">
                      <Button onClick={handleCreateBoard} disabled={isCreatingBoard}>
                        {isCreatingBoard && <Loader2 className="w-4 h-4 animate-spin mr-2" />}
                        Créer le plateau
                      </Button>
                      <span className="text-xs text-muted-foreground mt-1 text-center opacity-70">
                        POST /boards
                      </span>
                    </div>
                  </div>
                </DialogContent>
              </Dialog>
            </div>
          </div>

          <div className="flex justify-center mt-6 pt-4 border-t border-border">
            <div className="flex flex-col">
              <Button
                onClick={handleConfirm}
                disabled={!selectedPlayerId || !selectedBoardId}
                className="bg-gradient-primary hover:opacity-90 transition-opacity"
              >
                Créer la partie
              </Button>
              <span className="text-xs text-muted-foreground mt-1 text-center opacity-70">
                POST /games
              </span>
            </div>
          </div>
        </div>
      </Card>
    </div>
  );
};