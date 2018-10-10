package com.phi.battleshipapp;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "todayDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "GMT+2")
    private Date todayDate;

    @OneToMany(mappedBy = "game", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers;

    Game() {
        this.todayDate = new Date();
    }

    public void addPlayer(GamePlayer gamePlayer){
        gamePlayer.setGame(this);
        gamePlayers.add(gamePlayer);
    }

    public List<Player> getPlayers(){
        return gamePlayers.stream().map(gameplayer->gameplayer.getPlayer()).collect(toList());
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
