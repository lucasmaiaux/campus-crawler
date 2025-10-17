import React from 'react';
import { Card } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Player } from '@/types/game';
import { Heart, Sword, Shield, Zap } from 'lucide-react';

interface PlayerStatsProps {
  player: Player;
}

export const PlayerStats: React.FC<PlayerStatsProps> = ({ player }) => {
  const healthPercentage = (player.health / player.maxHealth) * 100;
  
  const getHealthColor = (percentage: number) => {
    return 'bg-red-500';
  };

  return (
    <Card className="bg-gradient-card shadow-card border-border">
      <div className="p-6 space-y-4">
        <div className="text-center">
          <h3 className="text-xl font-semibold text-foreground mb-1">{player.name}</h3>
          <Badge 
            variant="secondary" 
            className={player.type === 'Warrior' ? 'bg-destructive/20 text-destructive' : 'bg-cell-spell/20 text-purple-300'}
          >
            {player.type === 'Warrior' ? (
              <><Sword className="w-3 h-3 mr-1" /> Guerrier</>
            ) : (
              <><Zap className="w-3 h-3 mr-1" /> Magicien</>
            )}
          </Badge>
        </div>

        <div className="space-y-3">
          {/* Health Bar */}
          <div className="space-y-2">
            <div className="flex items-center justify-between text-sm">
              <div className="flex items-center gap-1">
                <Heart className="w-4 h-4 text-red-500" />
                <span className="text-foreground font-medium">Points de vie</span>
              </div>
              <span className="text-muted-foreground">
                {player.health} / {player.maxHealth}
              </span>
            </div>
            <div className="w-full bg-health-bar-bg rounded-full h-3 overflow-hidden">
              <div
                className={`h-full transition-all duration-500 ease-out ${getHealthColor(healthPercentage)}`}
                style={{ width: `${healthPercentage}%` }}
              />
            </div>
          </div>

          {/* Attack Points */}
          <div className="flex items-center p-3 bg-secondary/50 rounded-lg">
            <div className="flex items-center gap-2 flex-1">
              <Sword className="w-4 h-4 text-destructive" />
              <span className="text-sm font-medium">Attaque</span>
            </div>
            <div className="flex items-center justify-between flex-1">
              <span className="text-base font-bold text-foreground">
                {player.base_attack + (player.offensiveEquipment?.attack || 0)}
              </span>
              <span className="text-xs text-muted-foreground">
                ({player.base_attack}{player.offensiveEquipment?.attack ? ` + ${player.offensiveEquipment.attack}` : ''})
              </span>
            </div>
          </div>

          {/* Defense Points */}
          <div className="flex items-center p-3 bg-secondary/50 rounded-lg">
            <div className="flex items-center gap-2 flex-1">
              <Shield className="w-4 h-4 text-success" />
              <span className="text-sm font-medium">Défense</span>
            </div>
            <div className="flex items-center justify-between flex-1">
              {(() => {
                const totalDefenseBonus =
                  (player.defensiveEquipmentHelmet?.defense || 0) +
                  (player.defensiveEquipmentArmor?.defense || 0) +
                  (player.defensiveEquipmentShield?.defense || 0);
                const totalDefense = player.base_defense + totalDefenseBonus;

                return (
                  <>
                    <span className="text-base font-bold text-foreground">{totalDefense}</span>
                    <span className="text-xs text-muted-foreground">
                      ({player.base_defense}{totalDefenseBonus > 0 ? ` + ${totalDefenseBonus}` : ''})
                    </span>
                  </>
                );
              })()}
            </div>
          </div>

          {/* Equipment */}
          <div className="space-y-2">
            <h4 className="text-sm font-medium text-muted-foreground flex items-center gap-1">
              <Shield className="w-4 h-4" />
              Équipement
            </h4>
            
            <div className="grid grid-cols-1 gap-2">
              {player.offensiveEquipment ? (
                <div className="flex items-center justify-between p-2 bg-cell-weapon/20 border border-cell-weapon/30 rounded">
                  <div>
                    <div className="flex items-center gap-1">
                      <span className="text-sm font-medium text-foreground">{player.offensiveEquipment.name}</span>
                      {player.offensiveEquipment.attack && (
                        <span className="text-sm font-bold text-destructive flex items-center gap-1">
                          (+{player.offensiveEquipment.attack} <Sword className="w-3 h-3" />)
                        </span>
                      )}
                    </div>
                    <div className="text-xs text-muted-foreground">Arme offensive</div>
                  </div>
                </div>
              ) : (
                <div className="p-2 text-center text-xs text-muted-foreground bg-muted/30 rounded border-dashed border border-muted-foreground/30">
                  Aucune arme offensive
                </div>
              )}

              {/* Helmet */}
              {player.defensiveEquipmentHelmet ? (
                <div className="flex items-center justify-between p-2 bg-cell-armor/20 border border-cell-armor/30 rounded">
                  <div>
                    <div className="flex items-center gap-1">
                      <span className="text-sm font-medium text-foreground">{player.defensiveEquipmentHelmet.name}</span>
                      {player.defensiveEquipmentHelmet.defense && (
                        <span className="text-sm font-bold text-success flex items-center gap-1">
                          (+{player.defensiveEquipmentHelmet.defense} <Shield className="w-3 h-3" />)
                        </span>
                      )}
                    </div>
                    <div className="text-xs text-muted-foreground">Casque</div>
                  </div>
                </div>
              ) : (
                <div className="p-2 text-center text-xs text-muted-foreground bg-muted/30 rounded border-dashed border border-muted-foreground/30">
                  Aucun casque
                </div>
              )}

              {/* Armor */}
              {player.defensiveEquipmentArmor ? (
                <div className="flex items-center justify-between p-2 bg-cell-armor/20 border border-cell-armor/30 rounded">
                  <div>
                    <div className="flex items-center gap-1">
                      <span className="text-sm font-medium text-foreground">{player.defensiveEquipmentArmor.name}</span>
                      {player.defensiveEquipmentArmor.defense && (
                        <span className="text-sm font-bold text-success flex items-center gap-1">
                          (+{player.defensiveEquipmentArmor.defense} <Shield className="w-3 h-3" />)
                        </span>
                      )}
                    </div>
                    <div className="text-xs text-muted-foreground">Armure</div>
                  </div>
                </div>
              ) : (
                <div className="p-2 text-center text-xs text-muted-foreground bg-muted/30 rounded border-dashed border border-muted-foreground/30">
                  Aucune armure
                </div>
              )}

              {/* Shield */}
              {player.defensiveEquipmentShield ? (
                <div className="flex items-center justify-between p-2 bg-cell-armor/20 border border-cell-armor/30 rounded">
                  <div>
                    <div className="flex items-center gap-1">
                      <span className="text-sm font-medium text-foreground">{player.defensiveEquipmentShield.name}</span>
                      {player.defensiveEquipmentShield.defense && (
                        <span className="text-sm font-bold text-success flex items-center gap-1">
                          (+{player.defensiveEquipmentShield.defense} <Shield className="w-3 h-3" />)
                        </span>
                      )}
                    </div>
                    <div className="text-xs text-muted-foreground">Bouclier</div>
                  </div>
                </div>
              ) : (
                <div className="p-2 text-center text-xs text-muted-foreground bg-muted/30 rounded border-dashed border border-muted-foreground/30">
                  Aucun bouclier
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </Card>
  );
};