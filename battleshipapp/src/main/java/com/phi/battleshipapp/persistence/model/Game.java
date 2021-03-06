package com.phi.battleshipapp.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "todayDate")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "GMT+2")
    private Date todayDate;

//    public String toStringDate(Date date){
//        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");
//        return dateFormat.format(date);
//    }

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    Set<GamePlayer> gamePlayers = new LinkedHashSet();

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    Set<Score> scores = new LinkedHashSet<>();

    //    @JsonIgnore
    public Game() {
        this.todayDate = new Date();
    }

    public void addGamePlayer(GamePlayer gamePlayer){
        gamePlayer.setGame(this);
        gamePlayers.add(gamePlayer);
    }

    public List<Player> getPlayers() {
        return gamePlayers.stream().map(gamePlayer -> gamePlayer.getPlayer()).collect(toList());
    }

    public void addScore(Score score) {
        score.setGame(this);
        scores.add(score);
    }

    public Set<Score> getScores() {
        return scores;
    }

    @Override
    public String toString() {
        return "Game starting date " + this.id + this.todayDate;
    }

    public Date getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(Date todayDate) {
        this.todayDate = todayDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
