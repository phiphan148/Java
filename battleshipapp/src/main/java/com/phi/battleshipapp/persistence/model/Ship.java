package com.phi.battleshipapp.persistence.model;

import javax.persistence.*;
import javax.persistence.GenerationType;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String shipType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name = "location")
    private List<String> location = new ArrayList<>();

    public Ship(){}

    public Ship(String shipType, GamePlayer gamePlayer, List<String> locations){
        this.shipType = shipType;
        this.gamePlayer = gamePlayer;
        this.location = locations;
    }

    @Override
    public String toString() {
        return "Game starting date " + this.id + this.shipType + this.gamePlayer + this.location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public List<String> getLocation() {
        return location;
    }

    public void setLocations(List<String> location) {
        this.location = location;
    }
}
