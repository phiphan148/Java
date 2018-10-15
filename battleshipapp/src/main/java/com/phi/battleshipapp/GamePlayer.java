package com.phi.battleshipapp;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.*;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Entity
public class GamePlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "GMT+2")
    private Date date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @OneToMany(mappedBy = "gamePlayer", fetch=FetchType.EAGER)
    Set<Ship> ships = new LinkedHashSet<>();

    GamePlayer(){}

    GamePlayer(Game game, Player player){
        this.date = new Date();
        this.game = game;
        this.player = player;
    }

    public void addShip(Ship ship){
        ship.setGamePlayer(this);
        ships.add(ship);
    }

    public Set<Ship> getShips(){
        return ships;
    }

    @Override
    public String toString(){
        return "Game Player" + this.date + this.game + this.player + this.ships;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
