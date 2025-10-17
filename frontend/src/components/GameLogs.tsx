import React, { useEffect, useRef } from 'react';
import { Card } from '@/components/ui/card';
import { GameLog } from '@/types/game';
import { formatDistanceToNow, parseISO } from 'date-fns';
import { fr } from 'date-fns/locale';
import { Scroll } from 'lucide-react';

interface GameLogsProps {
  logs: GameLog[];
  isLoading?: boolean;
  gameId?: number;
}

export const GameLogs: React.FC<GameLogsProps> = ({ logs, isLoading = false, gameId }) => {
  const scrollRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (scrollRef.current) {
      scrollRef.current.scrollTop = scrollRef.current.scrollHeight;
    }
  }, [logs]);

  return (
    <Card className="bg-gradient-card shadow-card border-border h-full flex flex-col">
      <div className="p-4 flex-shrink-0">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-2">
            <Scroll className="w-4 h-4 text-accent" />
            <h3 className="text-lg font-semibold text-foreground">Journal</h3>
            {isLoading && (
              <div className="w-2 h-2 bg-accent rounded-full animate-pulse"></div>
            )}
          </div>
          <span className="text-xs text-muted-foreground opacity-70">GET /games/{gameId || '{gameId}'}/logs</span>
        </div>
      </div>

      <div className="flex-1 p-4 pt-0 min-h-0">
        <div 
          ref={scrollRef}
          className="h-full w-full overflow-y-auto bg-secondary/10 rounded-lg border border-border/50 p-4 space-y-2"
        >
          {logs.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              <Scroll className="w-8 h-8 mx-auto mb-2 opacity-50" />
              <p className="text-sm">Aucun événement pour le moment...</p>
              <p className="text-xs">Les actions du jeu apparaîtront ici</p>
            </div>
          ) : (
            logs.map((log) => {
              let timestamp = 'il y a quelques instants';
              
              try {
                if (log.timestamp) {
                  const date = parseISO(log.timestamp);
                  timestamp = formatDistanceToNow(date, { 
                    addSuffix: true, 
                    locale: fr 
                  });
                }
              } catch (error) {
                // Keep default timestamp if parsing fails
              }

              return (
                <div
                  key={log.id}
                  className="group p-3 bg-secondary/30 hover:bg-secondary/50 rounded-lg border border-border/50 transition-colors duration-200"
                >
                  <div className="flex items-start justify-between gap-2">
                    <p className="text-sm text-foreground leading-relaxed flex-1">
                      {log.message}
                    </p>
                    <span className="text-xs text-muted-foreground whitespace-nowrap opacity-60 group-hover:opacity-100 transition-opacity">
                      {timestamp}
                    </span>
                  </div>
                </div>
              );
            })
          )}
        </div>
      </div>
        
      {logs.length > 0 && (
        <div className="p-4 pt-0 flex-shrink-0">
          <div className="text-center">
            <span className="text-xs text-muted-foreground">
              {logs.length} événement{logs.length > 1 ? 's' : ''}
            </span>
          </div>
        </div>
      )}
    </Card>
  );
};
