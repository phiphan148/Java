package com.phi.battleshipapp.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import static java.util.stream.Collectors.toList;

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstname;
    private String lastname;
    private String username;
    private String password;



    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    Set<Score> scores = new LinkedHashSet<>();

    public Player(){}

    public Player(String first, String last, String email, String password){
        this.firstname = first;
        this.lastname = last;
        this.username = email;
        this.password = password;
    }

//    public void addGamePlayer(GamePlayer gamePlayer){
//        gamePlayer.setPlayer(this);
//        gamePlayers.add(gamePlayer);
//    }
//

    public void addScore(Score score) {
        score.setPlayer(this);
        scores.add(score);
    }

    public Set<Score> getScores() {
        return scores;
    }

    public Score getScore(Game game){
        return scores.stream().filter(score -> score.getGame().equals(game)).findFirst().orElse(null);
    }

    @JsonIgnore
    public List<Game> getGames(){
        return gamePlayers.stream().map(gamePlayer->gamePlayer.getGame()).collect(toList());
    }

    @Override
    public String toString(){
        return this.id + ' ' + this.firstname + ' ' + this.lastname + ' ' + this.username;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
