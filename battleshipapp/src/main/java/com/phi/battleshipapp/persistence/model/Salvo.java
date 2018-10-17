package com.phi.battleshipapp.persistence.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Salvo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
}
