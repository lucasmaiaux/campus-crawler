package fr.campus.dungeoncrawlerapi.dto;

import lombok.Data;

@Data
public class CreateGameRequest {
    private Integer playerId;
    private Integer boardId;
}