package com.phi.battleshipapp.persistence.model;

import com.phi.battleshipapp.persistence.repo.*;
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

    @Autowired
    private ShipRepository shipRepo;

    @Autowired
    private SalvoRepository salvoRepo;

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
            ResponseEntity<Object> refuse = new ResponseEntity<>(makeResponseEntityMap("message", "Please log in to see this content"), HttpStatus.UNAUTHORIZED);
            gamePlayer.put("Refuse", refuse.getBody() + " Status:" + refuse.getStatusCodeValue());
        } else {
            List<Long> currentUserGpIds = getCurrentUser(authentication).gamePlayers.stream().map(gp -> gp.getId()).collect(toList());
            gamePlayer.put("CurrentUserGPIds", currentUserGpIds);
            if (!currentUserGpIds.contains(nn)) {
                ResponseEntity<Object> refuse = new ResponseEntity<>(makeResponseEntityMap("message", "You have no authorise to see this content"), HttpStatus.UNAUTHORIZED);
                gamePlayer.put("Refuse", refuse.getBody() + " Status:" + refuse.getStatusCodeValue());
            } else {
                GamePlayer gamePlayerById = gamePlayerRepo.findById(nn).get();
                List<Player> playerList = gamePlayerById.getGame().getPlayers();
                Set<GamePlayer> gamePlayerSet = gamePlayerById.getGame().gamePlayers;
                List<Player> opponents = opponentList(playerList, gamePlayerById.getPlayer());

                //DECLARE FOR TURN HISTORY
                List<Ship> opponentShipsForEachTurn = getGamePlayerOpponent(gamePlayerSet, gamePlayerById).getShips().stream().collect(toList());
                List<Ship> mainPlayerShipsForEachTurn = gamePlayerById.getShips().stream().collect(toList());
                List<Salvo> mainPlayerSalvos = gamePlayerById.salvos.stream().collect(toList());
                //SORT SALVO BY TURN
                Collections.sort(mainPlayerSalvos, Salvo.salvoByTurn);
                List<Salvo> opponentSalvos = getGamePlayerOpponent(gamePlayerSet, gamePlayerById).salvos.stream().collect(toList());
                //SORT SALVO BY TURN
                Collections.sort(opponentSalvos, Salvo.salvoByTurn);
                List<Ship> opponentShipSunk = new ArrayList<>();
                List<Ship> mainPlayerShipSunk = new ArrayList<>();
                List<Map<String, Object>> turnHistory = new ArrayList<>();
                List<String> opponentShipLocationGetHitList = new ArrayList<>();
                List<String> mainPlayerShipLocationGetHitList = new ArrayList<>();

                //TURN HISTORY RETURN
                for (int i = 0; i < mainPlayerSalvos.size(); i++) {
                    int turn = 0;
                    turn = mainPlayerSalvos.get(i).getTurn();
                    Map<String, Object> historyTurn;
                    List<Map<String, Object>> turnHistoryMapListOpponent = new ArrayList<>();
                    List<Map<String, Object>> turnHistoryMapListMainPlayer = new ArrayList<>();

                    if (i < opponentSalvos.size()) {
                        getShipInfo(opponentShipLocationGetHitList, mainPlayerSalvos.get(i), opponentShipsForEachTurn, opponentShipSunk,
                                turnHistoryMapListOpponent);
                        getShipInfo(mainPlayerShipLocationGetHitList, opponentSalvos.get(i), mainPlayerShipsForEachTurn, mainPlayerShipSunk,
                                turnHistoryMapListMainPlayer);
                    }

                    if (i >= opponentSalvos.size()) {
                        getShipInfo(opponentShipLocationGetHitList, mainPlayerSalvos.get(i), opponentShipsForEachTurn, opponentShipSunk,
                                turnHistoryMapListOpponent);
                    }

                    //FINAL ADD
                    historyTurn = makeTurnHistoryMap(turn, turnHistoryMapListOpponent, turnHistoryMapListMainPlayer, opponentShipsForEachTurn.size() - opponentShipSunk.size(), mainPlayerShipsForEachTurn.size() - mainPlayerShipSunk.size());
                    turnHistory.add(historyTurn);
                }

                gamePlayer.put("turnHistory", turnHistory);
                gamePlayer.put("game", makeGameMap(gamePlayerById.getGame()));
                gamePlayer.put("mainPlayer", makePlayerMap(gamePlayerById.getPlayer()));
                gamePlayer.put("opponent", playerList(opponents));
                gamePlayer.put("mainPlayerShips", shipsList(gamePlayerById.getShips()));
                gamePlayer.put("mainPlayerSalvos", salvoList(gamePlayerById.getSalvos()));
                gamePlayer.put("opponentSalvos", salvoList(getGamePlayerOpponent(gamePlayerSet, gamePlayerById).getSalvos()));
            }
        }

        return gamePlayer;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> register(
            @RequestParam String firstname, @RequestParam String lastname,
            @RequestParam String username, @RequestParam String password) {

        if (firstname.isEmpty() || lastname.isEmpty() || username.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>(makeResponseEntityMap("error", "Missing data"), HttpStatus.FORBIDDEN);
        }

        if (playerRepo.findByUsername(username) != null) {
            return new ResponseEntity<>(makeResponseEntityMap("error", "Email already in use"), HttpStatus.FORBIDDEN);
        }

        playerRepo.save(new Player(firstname, lastname, username, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/players", method = RequestMethod.GET)
    public List<String> playerUserNameList() {
        return playerRepo.findAll()
                .stream().map(Player::getUsername).collect(toList());
    }

    @RequestMapping(value = "/games", method = RequestMethod.POST)
    public ResponseEntity<Object> addGame(Authentication authentication) {
        if (!checkLogin(authentication)) {
            return new ResponseEntity<>(makeResponseEntityMap("message", "Please log in to add new game"), HttpStatus.UNAUTHORIZED);
        }

        Game newGame = new Game();
        GamePlayer gamePlayerThisGame = new GamePlayer();
        gamePlayerThisGame.setPlayer(getCurrentUser(authentication));
        gamePlayerRepo.save(gamePlayerThisGame);
        newGame.addGamePlayer(gamePlayerThisGame);
        gameRepo.save(newGame);

        return new ResponseEntity<>(makeResponseEntityMap("gamePlayerId", gamePlayerThisGame.getId()), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/game/{gameId}/players", method = RequestMethod.POST)
    public ResponseEntity<Object> joinGame(@PathVariable Long gameId, Authentication authentication) {
        if (!checkLogin(authentication)) {
            return new ResponseEntity<>(makeResponseEntityMap("message", "Please log in to join new game"), HttpStatus.UNAUTHORIZED);
        }

        List<Long> GameIds = gameRepo.findAll().stream().map(game -> game.getId()).collect(toList());
        if (!GameIds.contains(gameId)) {
            return new ResponseEntity<>(makeResponseEntityMap("message", "There is no game you have chosen"), HttpStatus.FORBIDDEN);
        }

        Game gameGetChosen = gameRepo.getOne(gameId);
        if (gameGetChosen.getPlayers().size() >= 2) {
            return new ResponseEntity<>(makeResponseEntityMap("message", "The game you have chosen is full"), HttpStatus.FORBIDDEN);
        }

        if (gameGetChosen.getPlayers().contains(getCurrentUser(authentication))) {
            return new ResponseEntity<>(makeResponseEntityMap("message", "You are already join this game"), HttpStatus.FORBIDDEN);
        }

        GamePlayer newGamePlayer = new GamePlayer(gameGetChosen, getCurrentUser(authentication));
        gamePlayerRepo.save(newGamePlayer);

        return new ResponseEntity<>(makeResponseEntityMap("gamePlayerId", newGamePlayer.getId()), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/games/players/{gamePlayerId}/ships", method = RequestMethod.POST)
    public ResponseEntity<Object> addNewShip(@PathVariable Long gamePlayerId, @RequestBody Ship ship, Authentication authentication) {
        if (!checkLogin(authentication)) {
            return new ResponseEntity<>(makeResponseEntityMap("message", "Please log in to create new ships"), HttpStatus.UNAUTHORIZED);
        }

        List<Long> gamePlayerIds = gamePlayerRepo.findAll().stream().map(gp -> gp.getId()).collect(toList());
        if (!gamePlayerIds.contains(gamePlayerId)) {
            return new ResponseEntity<>(makeResponseEntityMap("message", "There is no game player with the given ID"), HttpStatus.UNAUTHORIZED);
        }

        GamePlayer gamePlayerById = gamePlayerRepo.findById(gamePlayerId).get();
        Player playerByGpId = gamePlayerById.getPlayer();
        if (playerByGpId.getId() != getCurrentUser(authentication).getId()) {
            return new ResponseEntity<>(makeResponseEntityMap("message", "You are not authorise to create new ships"), HttpStatus.UNAUTHORIZED);
        }

        if (gamePlayerById.ships.size() > 5) {
            return new ResponseEntity<>(makeResponseEntityMap("message", "You cannot create more than 4 ships"), HttpStatus.FORBIDDEN);
        }


        ship.setGamePlayer(gamePlayerById);
        List<List<String>> shipLocation = gamePlayerById.ships.stream().map(shipinlist -> shipinlist.getLocation()).collect(toList());
        List<String> mainPlayerShipLocation = shipLocation.stream()
                .flatMap(location -> location.stream()).collect(toList());

        if (gamePlayerById.ships.stream().map(shipinList -> shipinList.getShipType()).collect(toList()).contains(ship.getShipType())) {
            return new ResponseEntity<>(makeResponseEntityMap("message", "Ship already was added before"), HttpStatus.FORBIDDEN);
        }

        List<String> overlapShip = new ArrayList<>();
        for (int i = 0; i < ship.getLocation().size(); i++) {
            if (mainPlayerShipLocation.contains(ship.getLocation().get(i))) {
                overlapShip.add(ship.getLocation().get(i));
            }
        }

        if (overlapShip.size() > 0) {
            return new ResponseEntity<>(makeResponseEntityMap("Overlapships", overlapShip), HttpStatus.FORBIDDEN);
        } else {
            shipRepo.save(ship);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/games/players/{gamePlayerId}/salvos", method = RequestMethod.POST)
    public ResponseEntity<Object> addNewSalvo(@PathVariable Long gamePlayerId, @RequestBody Salvo salvo, Authentication authentication) {
        if (!checkLogin(authentication)) {
            return new ResponseEntity<>(makeResponseEntityMap("message", "Please log in to create new ships"), HttpStatus.UNAUTHORIZED);
        }

        List<Long> gamePlayerIds = gamePlayerRepo.findAll().stream().map(gp -> gp.getId()).collect(toList());
        if (!gamePlayerIds.contains(gamePlayerId)) {
            return new ResponseEntity<>(makeResponseEntityMap("message", "There is no game player with the given ID"), HttpStatus.UNAUTHORIZED);
        }

        GamePlayer gamePlayerById = gamePlayerRepo.findById(gamePlayerId).get();
        Player playerByGpId = gamePlayerById.getPlayer();
        if (playerByGpId.getId() != getCurrentUser(authentication).getId()) {
            return new ResponseEntity<>(makeResponseEntityMap("message", "You are not authorise to create new ships"), HttpStatus.UNAUTHORIZED);
        }

        salvo.setGamePlayer(gamePlayerById);
        salvo.setTurn(3);

        List<List<String>> salvoLocation = gamePlayerById.salvos.stream().map(salvoL -> salvoL.getTurnLocation()).collect(toList());
        List<String> mainPlayerSalvoLocation = salvoLocation.stream()
                .flatMap(location -> location.stream()).collect(toList());

        List<String> overlapSalvo = new ArrayList<>();
        for (int i = 0; i < salvo.getTurnLocation().size(); i++) {
            if (mainPlayerSalvoLocation.contains(salvo.getTurnLocation().get(i))) {
                overlapSalvo.add(salvo.getTurnLocation().get(i));
            }
        }

        List<Integer> salvoTurn = gamePlayerById.salvos.stream().map(salvoL -> salvoL.getTurn()).collect(toList());
        if (salvoTurn.contains(salvo.getTurn())) {
            return new ResponseEntity<>(makeResponseEntityMap("Cannot use this turn", salvo.getTurn()), HttpStatus.FORBIDDEN);
        }

        if (overlapSalvo.size() > 0) {
            return new ResponseEntity<>(makeResponseEntityMap("OverlapSalvo", overlapSalvo), HttpStatus.FORBIDDEN);
        } else {
            salvoRepo.save(salvo);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/games/players/{gamePlayerId}/ships", method = RequestMethod.GET)
    public List<Map<String, Object>> shipsNewCreate(@PathVariable Long gamePlayerId) {
        return shipsList(gamePlayerRepo.findById(gamePlayerId).get().ships);
    }

    @RequestMapping(value = "/game/{gameId}/players", method = RequestMethod.GET)
    public List<Map<String, Object>> gameHasBeenJoin(@PathVariable Long gameId) {
        return playerList(gameRepo.findById(gameId).get().getPlayers());
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

    private Map<String, Object> makeResponseEntityMap(String key, Object value) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(key, value);
        return map;
    }

    private Map<String, Object> makeTurnHistoryMap(int turn, List<Map<String, Object>> opponentShipGetHitList, List<Map<String, Object>> mainPlayerShipGetHitList, int opponentShipsLeft, int mainPlayerShipsLeft) {
        Map<String, Object> turnHistoryMap = new LinkedHashMap<>();
        turnHistoryMap.put("turn", turn);
        turnHistoryMap.put("mainPlayerShipGetHitList", mainPlayerShipGetHitList);
        turnHistoryMap.put("mainPlayerShipsLeft", mainPlayerShipsLeft);
        turnHistoryMap.put("opponentShipGetHitList", opponentShipGetHitList);
        turnHistoryMap.put("opponentShipsLeft", opponentShipsLeft);
        return turnHistoryMap;
    }

    private Map<String, Object> makeShipsGetHitMap(String shipType, int hitNumber, boolean sunk) {
        Map<String, Object> shipsGetHitMap = new LinkedHashMap<>();
        shipsGetHitMap.put("shipType", shipType);
        shipsGetHitMap.put("hitNumber", hitNumber);
        shipsGetHitMap.put("sunk", sunk);
        return shipsGetHitMap;
    }

    private void getShipInfo(List<String> locationGetHitList, Salvo salvo, List<Ship> shipListForEachTurn, List<Ship> shipsSunkList,
                             List<Map<String, Object>> turnHistoryMapList) {
        for (int k = 0; k < shipListForEachTurn.size(); k++) {
            List<String> locationGetHitEachTurn = new ArrayList<>();
            for (int j = 0; j < salvo.getTurnLocation().size(); j++) {
                if (shipListForEachTurn.get(k).getLocation().contains(salvo.getTurnLocation().get(j))) {
                    if (!locationGetHitList.contains(salvo.getTurnLocation().get(j))) {
                        locationGetHitList.add(salvo.getTurnLocation().get(j));
                    }
                    locationGetHitEachTurn.add(salvo.getTurnLocation().get(j));
                    List<String> shipLocationLeft = shipListForEachTurn.get(k).getLocation().stream().filter(lo -> !locationGetHitList.contains(lo)).collect(toList());

                    boolean checkOpponent;
                    if (shipLocationLeft.size() == 0) {
                        checkOpponent = true;
                        shipsSunkList.add(shipListForEachTurn.get(k));
                    } else {
                        checkOpponent = false;
                    }

                    String shipType = shipListForEachTurn.get(k).getShipType();
                    if (turnHistoryMapList.stream().filter(s -> s.containsValue(shipType)).collect(toList()).size() > 0) {
                        for (int z = 0; z < turnHistoryMapList.size(); z++) {
                            if (turnHistoryMapList.get(z).containsValue(shipType)) {
                                turnHistoryMapList.get(z).replace("hitNumber", locationGetHitEachTurn.size());
                                turnHistoryMapList.get(z).replace("sunk", checkOpponent);
                            }
                        }
                    } else {
                        Map<String, Object> opponentShipMap = makeShipsGetHitMap(shipListForEachTurn.get(k).getShipType(), locationGetHitEachTurn.size(), checkOpponent);
                        turnHistoryMapList.add(opponentShipMap);
                    }
                }
            }
        }
    }
}
