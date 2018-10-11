package com.phi.battleshipapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RestController
public class SalvoController {

    @Autowired
    private GameRepository repo;

    @RequestMapping(value = "api/games", method = RequestMethod.GET)
    public List<Object> gamedetails(){
        return repo
                .findAll().stream().map(map-> makeGameMap(map)).collect(toList());
    }

    private Map<String, Object> makePlayerMap(Player player){
        Map<String, Object> playermap = new LinkedHashMap<String, Object>();
        playermap.put("id", player.getId());
        playermap.put("created", player.getUsername());
        return playermap;
    }

    private Map<String, Object> makeGamePlayerMap(GamePlayer gamePlayer){
        Map<String, Object> gameplayermap = new LinkedHashMap<String, Object>();
        gameplayermap.put("id", gamePlayer.getId());
        gameplayermap.put("player", makePlayerMap(gamePlayer.getPlayer()));
        return gameplayermap;
    }

    private List<Map<String, Object>> gamePlayer (Set<GamePlayer> gamePlayers){
        return gamePlayers.stream().map(gamePlayer -> makeGamePlayerMap(gamePlayer)).collect(toList());
    }

    private Map<String, Object> makeGameMap(Game game) {
        Map<String, Object> gameplayermap = new LinkedHashMap<String, Object>();
        gameplayermap.put("id", game.getId());
        gameplayermap.put("created", game.getTodayDate());
        gameplayermap.put("gamePlayers", gamePlayer(game.gamePlayers));
        return gameplayermap;
    }

}