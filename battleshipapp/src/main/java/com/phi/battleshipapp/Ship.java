package com.phi.battleshipapp;

import javax.persistence.*;
import javax.persistence.GenerationType;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String shipType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gameplayer_id")
    private GamePlayer gamePlayer;

    @ElementCollection
//    @CollectionTable(name="ship_location", joinColumns=@JoinColumn(name="ship_id"))
    @Column(name = "location")
    private List<String> locations = new ArrayList<>();

    Ship(){}

    Ship(String shipType, GamePlayer gamePlayer, List<String> locations){
        this.shipType = shipType;
        this.gamePlayer = gamePlayer;
        this.locations = locations;
    }

    @Override
    public String toString() {
        return "Game starting date " + this.id + this.shipType + this.gamePlayer + this.locations;
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

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }
}
