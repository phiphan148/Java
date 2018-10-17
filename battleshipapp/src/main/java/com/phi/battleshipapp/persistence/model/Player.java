package com.phi.battleshipapp.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname;
    private String lastname;
    private String username;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers = new LinkedHashSet<>();

    public Player(){}

    public Player(String first, String last, String email){
        this.firstname = first;
        this.lastname = last;
        this.username = email;
    }

//    public void addGame(GamePlayer gamePlayer){
//        gamePlayer.setPlayer(this);
//        gamePlayers.add(gamePlayer);
//    }
//
    @JsonIgnore
    public List<Game> getGames(){
        return gamePlayers.stream().map(gamePlayer->gamePlayer.getGame()).collect(toList());
    }

    @Override
    public String toString(){
        return this.id + ' ' + this.firstname + ' ' + this.lastname + ' ' + this.username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String email) {
        this.username = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
