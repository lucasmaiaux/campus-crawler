package fr.campus.dungeoncrawlerapi.controller;

import fr.campus.dungeoncrawlerapi.domain.game.board.Board;
import fr.campus.dungeoncrawlerapi.dto.BoardDTO;
import fr.campus.dungeoncrawlerapi.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * GET    /boards           - Liste des plateaux
 * GET    /boards/{id}      - Détails d'un plateau
 * POST   /boards           - Création d'un plateau
 * DELETE /boards/{id}      - Supprime un plateau
 */
@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // [GET] /boards : Liste des plateaux
    @GetMapping("/boards")
    public Iterable<Board> getBoards() {
        return boardService.getBoards();
    }

    // [GET] /boards/{id} : Détails d'un plateau
    @GetMapping("/boards/{id}")
    public Optional<Board> getBoard(@PathVariable Integer id) {
        return boardService.getBoard(id);
    }

    // [POST] /boards : Création d'un plateau
    @PostMapping("/boards")
    public Board createBoard(@RequestBody BoardDTO boardDTO) {
        return boardService.createBoardFromDTO(boardDTO);
    }

    // [DELETE] /boards/{id} : Supprime un plateau
    @DeleteMapping("/boards/{id}")
    public void deleteBoard(@PathVariable Integer id) {
        boardService.deleteBoard(id);
    }
}
