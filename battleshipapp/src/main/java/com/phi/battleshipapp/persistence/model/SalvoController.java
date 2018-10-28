package com.phi.battleshipapp.persistence.model;

import com.phi.battleshipapp.persistence.repo.GamePlayerRepository;
import com.phi.battleshipapp.persistence.repo.GameRepository;
import com.phi.battleshipapp.persistence.repo.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private GamePlayerRepository gamePlayerRepo;

    @Autowired
    private PlayerRepository playerRepo;

    @RequestMapping(value = "/games", method = RequestMethod.GET)
    public Map<String, Object> makeGameMapWithCurrentUser(Authentication authentication) {
        Map<String, Object> gameMapWithCurrentUser = new LinkedHashMap<>();
        List<Object> games = gameRepo
                .findAll().stream().map(game -> makeGameMap(game)).collect(toList());
        List<String> usersEmail = playerRepo.findAll()
                .stream().map(player -> player.getUsername()).collect(toList());
        if (!checkLogin(authentication) || !usersEmail.contains(getCurrentUser(authentication).getUsername())) {
            gameMapWithCurrentUser.put("currentPlayer", null);
        } else {
            gameMapWithCurrentUser.put("currentPlayer", makePlayerMap(getCurrentUser(authentication)));
        }
        gameMapWithCurrentUser.put("games", games);
        return gameMapWithCurrentUser;
    }

    @RequestMapping(value = "/scores", method = RequestMethod.GET)
    public List<Object> scoreDetails() {
        return gamePlayerRepo
                .findAll().stream().map(gameplayer -> makeGamePlayerMap(gameplayer)).collect(toList());
    }

    @RequestMapping("/game_view/{nn}")
    public Map<String, Object> findGamePlayer(@PathVariable Long nn, Authentication authentication) {
        Map<String, Object> gamePlayer = new LinkedHashMap<>();
        if (!checkLogin(authentication)) {
            gamePlayer.put("test", null);
            ResponseEntity<Object> refuse = new ResponseEntity<>("You have no authorise to see this content", HttpStatus.FORBIDDEN);
            gamePlayer.put("refuse", refuse);
        } else {
            List<Long> currentUserGpIds = getCurrentUser(authentication).gamePlayers.stream().map(gp -> gp.getId()).collect(toList());
            gamePlayer.put("test", currentUserGpIds);
            for (int i = 0; i < currentUserGpIds.size(); i++) {
                GamePlayer gamePlayerById = gamePlayerRepo.findById(currentUserGpIds.get(i)).get();
                List<Player> playerList = gamePlayerById.getGame().getPlayers();
                Set<GamePlayer> gamePlayerSet = gamePlayerById.getGame().gamePlayers;
                List<Player> opponents = opponentList(playerList, gamePlayerById.getPlayer());

                List<List<String>> opponentShipList = getGamePlayerOpponent(gamePlayerSet, gamePlayerById).
                        getShips().stream().map(ship -> ship.getLocation()).collect(toList());

                List<List<String>> mainPlayerSalvoLocationList = gamePlayerById.getSalvos()
                        .stream().map(salvo -> salvo.getTurnLocation()).collect(toList());

                List<String> mainPlayerSalvoLocation = mainPlayerSalvoLocationList.stream()
                        .flatMap(x -> x.stream())
                        .collect(Collectors.toList());

                List<String> opponentShip = opponentShipList.stream()
                        .flatMap(x -> x.stream())
                        .collect(Collectors.toList());

                List<String> opponentShipGetHit = new ArrayList<>();

                for(int j=0; j<mainPlayerSalvoLocation.size(); j++){
                    if(opponentShip.contains(mainPlayerSalvoLocation.get(j))){
                        opponentShipGetHit.add(mainPlayerSalvoLocation.get(j));
                    }
                }

                gamePlayer.put("game", makeGameMap(gamePlayerById.getGame()));
                gamePlayer.put("mainPlayer", makePlayerMap(gamePlayerById.getPlayer()));
                gamePlayer.put("opponent", playerList(opponents));
                gamePlayer.put("mainPlayerShips", shipsList(gamePlayerById.getShips()));
                gamePlayer.put("mainPlayerSalvos", salvoList(gamePlayerById.getSalvos()));
                gamePlayer.put("opponentShipGetHit", opponentShipGetHit);
                gamePlayer.put("opponentSalvos", salvoList(getGamePlayerOpponent(gamePlayerSet, gamePlayerById).getSalvos()));
            }
        }

        return gamePlayer;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String firstname, @RequestParam String lastname,
            @RequestParam String username, @RequestParam String password) {

        if (firstname.isEmpty() || lastname.isEmpty() || username.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (playerRepo.findByUsername(username) != null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }

        playerRepo.save(new Player(firstname, lastname, username, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/players", method = RequestMethod.GET)
    public List<String> test() {
        return playerRepo.findAll()
                .stream().map(player -> player.getUsername()).collect(toList());
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
//        salvoMap.put("player", salvo.getGamePlayer().getPlayer().getId());
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
        if (gamePlayer.getScore() != null) {
            gamePlayerMap.put("score", makeScoreMap(gamePlayer.getScore()));
        } else {
            gamePlayerMap.put("score", null);
        }
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
        gamePlayerMap.put("gamePlayers", gamePlayer(game.gamePlayers));
        return gamePlayerMap;
    }

    private Map<String, Object> makeScoreMap(Score score) {
        Map<String, Object> scoreMap = new LinkedHashMap<>();
        scoreMap.put("score", score.getScore());
        return scoreMap;
    }

    private Player getCurrentUser(Authentication authentication) {
        return playerRepo.findByUsername(authentication.getName());
    }

    private Boolean checkLogin(Authentication authentication) {
        if (authentication == null) {
            return false;
        } else {
            return true;
        }
    }

}
