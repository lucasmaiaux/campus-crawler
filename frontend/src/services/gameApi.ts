import { GameState, GameLog, CreateGameRequest, Player, Board, PlayerDTO, PlayerNameDTO, BoardDTO } from '@/types/game';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:9060';

class GameApiService {
  private async handleResponse<T>(response: Response): Promise<T> {
    if (!response.ok) {
      const errorText = await response.text();
      throw new ApiError(errorText || `HTTP ${response.status}`, response.status);
    }
    return response.json();
  }

  async createGame(request: CreateGameRequest): Promise<GameState> {
    const response = await fetch(`${API_BASE_URL}/games`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(request),
    });
    return this.handleResponse<GameState>(response);
  }

  async getGameState(gameId: number): Promise<GameState> {
    const response = await fetch(`${API_BASE_URL}/games/${gameId}`);
    return this.handleResponse<GameState>(response);
  }

  async movePlayer(gameId: number): Promise<GameState> {
    const response = await fetch(`${API_BASE_URL}/games/${gameId}/move`, {
      method: 'PUT',
    });
    return this.handleResponse<GameState>(response);
  }

  async interact(gameId: number): Promise<GameState> {
    const response = await fetch(`${API_BASE_URL}/games/${gameId}/interact`, {
      method: 'POST',
    });
    return this.handleResponse<GameState>(response);
  }

  async getGameLogs(gameId: number): Promise<GameLog[]> {
    const response = await fetch(`${API_BASE_URL}/games/${gameId}/logs`);
    const rawLogs = await this.handleResponse<string[]>(response);
    
    // Convert string array to GameLog objects
    return rawLogs.map((message, index) => ({
      id: index,
      message,
      timestamp: new Date().toISOString() // API doesn't provide timestamps
    }));
  }

  async getAllGames(): Promise<GameState[]> {
    const response = await fetch(`${API_BASE_URL}/games`);
    return this.handleResponse<GameState[]>(response);
  }

  async deleteGame(gameId: number): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/games/${gameId}`, {
      method: 'DELETE',
    });
    if (!response.ok) {
      const errorText = await response.text();
      throw new ApiError(errorText || `HTTP ${response.status}`, response.status);
    }
  }

  async getPlayers(): Promise<Player[]> {
    const response = await fetch(`${API_BASE_URL}/players`);
    return this.handleResponse<Player[]>(response);
  }

  async createPlayer(playerNameDTO: PlayerNameDTO): Promise<Player> {
    const response = await fetch(`${API_BASE_URL}/players/name`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(playerNameDTO),
    });
    return this.handleResponse<Player>(response);
  }

  async getBoards(): Promise<Board[]> {
    const response = await fetch(`${API_BASE_URL}/boards`);
    return this.handleResponse<Board[]>(response);
  }

  async createBoard(boardDTO: BoardDTO): Promise<Board> {
    const response = await fetch(`${API_BASE_URL}/boards`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(boardDTO),
    });
    return this.handleResponse<Board>(response);
  }

  async deletePlayer(playerId: number): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/players/${playerId}`, {
      method: 'DELETE',
    });
    if (!response.ok) {
      const errorText = await response.text();
      throw new ApiError(errorText || `HTTP ${response.status}`, response.status);
    }
  }

  async deleteBoard(boardId: number): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/boards/${boardId}`, {
      method: 'DELETE',
    });
    if (!response.ok) {
      const errorText = await response.text();
      throw new ApiError(errorText || `HTTP ${response.status}`, response.status);
    }
  }
}

class ApiError extends Error {
  status?: number;
  
  constructor(message: string, status?: number) {
    super(message);
    this.name = 'ApiError';
    this.status = status;
  }
}

export const gameApi = new GameApiService();
export { ApiError };