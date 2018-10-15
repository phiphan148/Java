package com.phi.battleshipapp;

import jdk.nashorn.internal.runtime.options.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepo;
    private GamePlayerRepository gamePlayerRepo;

    @RequestMapping(value = "/games", method = RequestMethod.GET)
    public List<Object> gamedetails() {
        return gameRepo
                .findAll().stream().map(map -> makeGameMap(map)).collect(toList());
    }

    @RequestMapping("/game_view/{nn}")
    public Map<String, Object> findgamePlayer(@PathVariable Long nn) {
        Map<String, Object> gameplayer = new LinkedHashMap<>();
                GamePlayer gamePlayerId = gamePlayerRepo.findById(nn).get();
        gameplayer.put("id", nn);
        gameplayer.put("game", makeGameMap(gamePlayerId.getGame()));
        return gameplayer;
    }

    private Map<String, Object> makePlayerMap(Player player) {
        Map<String, Object> playermap = new LinkedHashMap<>();
        playermap.put("id", player.getId());
        playermap.put("email", player.getUsername());
        return playermap;
    }

    private Map<String, Object> makeGamePlayerMap(GamePlayer gamePlayer) {
        Map<String, Object> gameplayermap = new LinkedHashMap<>();
        gameplayermap.put("id", gamePlayer.getId());
        gameplayermap.put("player", makePlayerMap(gamePlayer.getPlayer()));
        return gameplayermap;
    }

    private List<Map<String, Object>> gamePlayer(Set<GamePlayer> gamePlayers) {
        return gamePlayers.stream().map(gamePlayer -> makeGamePlayerMap(gamePlayer)).collect(toList());
    }

    private Map<String, Object> makeGameMap(Game game) {
        Map<String, Object> gameplayermap = new LinkedHashMap<>();
        gameplayermap.put("id", game.getId());
        gameplayermap.put("created", game.getTodayDate());
        gameplayermap.put("gamePlayers", gamePlayer(game.gamePlayers));
        return gameplayermap;
    }

}