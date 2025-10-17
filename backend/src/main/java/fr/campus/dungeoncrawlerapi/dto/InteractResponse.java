package fr.campus.dungeoncrawlerapi.dto;

public record InteractResponse<T, U>(T game, U logs) {}