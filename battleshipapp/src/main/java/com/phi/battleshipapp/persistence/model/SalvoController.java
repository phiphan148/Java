package com.phi.battleshipapp.persistence.model;

import com.phi.battleshipapp.persistence.repo.GamePlayerRepository;
import com.phi.battleshipapp.persistence.repo.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private GamePlayerRepository gamePlayerRepo;

    @RequestMapping(value = "/games", method = RequestMethod.GET)
    public List<Object> gameDetails() {
        return gameRepo
                .findAll().stream().map(map -> makeGameMap(map)).collect(toList());
    }

    @RequestMapping("/game_view/{nn}")
    public Map<String, Object> findGamePlayer(@PathVariable Long nn) {
        Map<String, Object> gamePlayer = new LinkedHashMap<>();

        GamePlayer gamePlayerById = gamePlayerRepo.findById(nn).get();
        List<Player> playerList = gamePlayerById.getGame().getPlayers();
        Set<GamePlayer> gamePlayerSet = gamePlayerById.getGame().gamePlayers;
        List<Player> opponents = opponentList(playerList, gamePlayerById.getPlayer());

//        List<Game> gameIds = gamePlayerRepo.findAll().stream().map(gamePlayer -> gamePlayer.getGame()).collect(toList());
//        Game gameById = gameIds.stream().filter((game) -> game.getId() == nn).findAny().get();
        gamePlayer.put("game", makeGameMap(gamePlayerById.getGame()));
        gamePlayer.put("mainPlayer", makePlayerMap(gamePlayerById.getPlayer()));
        gamePlayer.put("opponent", playerList(opponents));
        gamePlayer.put("mainPlayerShips", shipsList(gamePlayerById.getShips()));
        gamePlayer.put("mainPlayerSalvos", salvoList(gamePlayerById.getSalvos()));
        gamePlayer.put("opponentSalvos", salvoList(getGamePlayerOpponent(gamePlayerSet, gamePlayerById).getSalvos()));

        return gamePlayer;
    }

    private GamePlayer getGamePlayerOpponent(Set<GamePlayer> gamePlayers, GamePlayer gamePlayer) {
        Stream<GamePlayer> gamePlayerOpponents = gamePlayers.stream()
                .filter(gp -> !gp.equals(gamePlayer));
        if (gamePlayerOpponents.count() == 0) {
            GamePlayer fakeOpponent = new GamePlayer();
            return fakeOpponent;
        } else {
            return
                    gamePlayers.stream()
                            .filter(gp -> !gp.equals(gamePlayer)).findAny().get();
        }
    }

    private Map<String, Object> makeSalvoMap(Salvo salvo) {
        Map<String, Object> salvoMap = new LinkedHashMap<>();
        salvoMap.put("turn", salvo.getTurn());
        salvoMap.put("player", salvo.getGamePlayer().getPlayer().getId());
        salvoMap.put("location", salvo.getTurnLocation());
        return salvoMap;
    }

    private List<Map<String, Object>> salvoList(Set<Salvo> salvos) {
        return salvos.stream().map(salvo -> makeSalvoMap(salvo)).collect(toList());
    }

    private List<Player> opponentList(List<Player> playerList, Player playerId) {
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).equals(playerId)) {
                playerList.remove(playerList.get(i));
            }
        }
        return playerList;
    }

    private Map<String, Object> makePlayerMap(Player player) {
        Map<String, Object> playerMap = new LinkedHashMap<>();
        playerMap.put("id", player.getId());
        playerMap.put("email", player.getUsername());
        return playerMap;
    }

    private List<Map<String, Object>> playerList(List<Player> players) {
        return players.stream().map(player -> makePlayerMap(player)).collect(toList());
    }

    private Map<String, Object> makeGamePlayerMap(GamePlayer gamePlayer) {
        Map<String, Object> gamePlayerMap = new LinkedHashMap<>();
        gamePlayerMap.put("id", gamePlayer.getId());
        gamePlayerMap.put("player", makePlayerMap(gamePlayer.getPlayer()));
        return gamePlayerMap;
    }

    private List<Map<String, Object>> gamePlayer(Set<GamePlayer> gamePlayers) {
        return gamePlayers.stream().map(gamePlayer -> makeGamePlayerMap(gamePlayer)).collect(toList());
    }

    private Map<String, Object> makeShipMap(Ship ship) {
        Map<String, Object> shipMap = new LinkedHashMap<>();
        shipMap.put("shipType", ship.getShipType());
        shipMap.put("shipLocation", ship.getLocation());
        return shipMap;
    }

    private List<Map<String, Object>> shipsList(Set<Ship> ships) {
        return ships.stream().map(ship -> makeShipMap(ship)).collect(toList());
    }

    private Map<String, Object> makeGameMap(Game game) {
        Map<String, Object> gamePlayerMap = new LinkedHashMap<>();
        gamePlayerMap.put("id", game.getId());
        gamePlayerMap.put("created", game.getTodayDate());
//        gamePlayerMap.put("gamePlayers", gamePlayer(game.gamePlayers));
        return gamePlayerMap;
    }

}