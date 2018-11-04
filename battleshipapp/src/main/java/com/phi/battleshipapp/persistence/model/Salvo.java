package com.phi.battleshipapp.persistence.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

@Entity
public class Salvo{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    private GamePlayer gamePlayer;

    private int turn;

    @ElementCollection
    @Column(name = "turnLocation")
    private List<String> turnLocation = new ArrayList<>();

    public Salvo(){}

    public Salvo(int turn, List<String> turnLocation){
        this.turn = turn;
        this.turnLocation = turnLocation;
    }


    @Override
    public String toString() {
        return "Game starting date " + this.id + this.gamePlayer + this.turnLocation + this.turn;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public List<String> getTurnLocation() {
        return turnLocation;
    }

    public void setTurnLocation(List<String> turnLocation) {
        this.turnLocation = turnLocation;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public static Comparator<Salvo> salvoByTurn = (s1, s2) -> {

        int turn1 = s1.getTurn();
        int turn2 = s2.getTurn();
        return turn1-turn2;

    };

}
