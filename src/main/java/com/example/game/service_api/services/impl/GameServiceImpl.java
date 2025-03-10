package com.example.game.service_api.services.impl;

import com.example.game.service_api.commons.entities.Game;
import com.example.game.service_api.commons.exceptions.GameException;
import com.example.game.service_api.repositories.GameRepository;
import com.example.game.service_api.services.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public Game saveGame(Game gameRequest) {
        return this.gameRepository.save(gameRequest);
    }

    @Override
    public Game getGameById(String id) {
        return this.gameRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new GameException(HttpStatus.NOT_FOUND, "Error finding game"));

    }

    @Override
    public Game updateGameByCriteria(String id, String name, Game gameRequest) {
        Game existingGame;

        if (id != null) {
            existingGame = gameRepository.findById(Long.valueOf(id))
                    .orElseThrow(() -> new GameException(HttpStatus.NOT_FOUND, "Game not found by ID"));
        } else if (name != null) {
            existingGame = (Game) gameRepository.findByName(name)
                    .orElseThrow(() -> new GameException(HttpStatus.NOT_FOUND, "Game not found by Name"));
        } else {
            throw new GameException(HttpStatus.BAD_REQUEST, "ID or Name must be provided");
        }

        if (gameRequest.getName() != null) {
            existingGame.setName(gameRequest.getName());
        }

        return gameRepository.save(existingGame);
    }

    @Override
    public Game deleteGameByCriteria(String id, String name) {
        Game gameToDelete;

        if (id != null && name != null) {
            // Buscar por ID y Name
            gameToDelete = gameRepository.findById(Long.valueOf(id))
                    .filter(g -> g.getName().equals(name))
                    .orElseThrow(() -> new GameException(HttpStatus.NOT_FOUND, "Game not found with matching ID and Name"));
        } else if (id != null) {
            gameToDelete = gameRepository.findById(Long.valueOf(id))
                    .orElseThrow(() -> new GameException(HttpStatus.NOT_FOUND, "Game not found by ID"));
        } else if (name != null) {
            gameToDelete = gameRepository.findByName(name)
                    .orElseThrow(() -> new GameException(HttpStatus.NOT_FOUND, "Game not found by Name"));
        } else {
            throw new GameException(HttpStatus.BAD_REQUEST, "ID or Name must be provided");
        }

        gameRepository.delete(gameToDelete);
        return gameToDelete;
    }

}
