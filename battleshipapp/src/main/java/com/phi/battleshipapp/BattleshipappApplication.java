package com.phi.battleshipapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class BattleshipappApplication {

    public static void main(String[] args) {
        SpringApplication.run(BattleshipappApplication.class, args);
    }

    @Bean
    public CommandLineRunner initGamePlayer(PlayersRepository playerrepository, GameRepository gamerepository, GamePlayerRepository gameplayerrepository, ShipRepository shiprepository){
        return (args) -> {
            Player player1 = new Player("Phi", "Phan", "phi@gmail.com");
            playerrepository.save(player1);
            Player player2 = new Player("Alain", "Phan", "alain@gmail.com");
            playerrepository.save(player2);
            Player player3 = new Player("Beatle", "Phan", "beatle@gmail.com");
            playerrepository.save(player3);

            Game game1 = new Game();
            gamerepository.save(game1);
            Game game2 = new Game();
            game2.setTodayDate(Date.from(new Date().toInstant().plusSeconds(3600)));
            gamerepository.save(game2);
            Game game3 = new Game();
            game3.setTodayDate(Date.from(new Date().toInstant().plusSeconds(7200)));
            gamerepository.save(game3);

            GamePlayer gamePlayer1 = new GamePlayer(game1,player2);
            gameplayerrepository.save(gamePlayer1);
            GamePlayer gamePlayer2 = new GamePlayer(game1, player3);
            gameplayerrepository.save(gamePlayer2);
            GamePlayer gamePlayer3 = new GamePlayer(game3, player1);
            gameplayerrepository.save(gamePlayer3);

            List<String> location1 = Arrays.asList("H3", "H2", "G1");
            List<String> location2 = Arrays.asList("G1", "H1", "G3");
            List<String> location3 = Arrays.asList("G5", "H6", "G4");

            Ship ship1 = new Ship("cruiser",gamePlayer1,location1);
            shiprepository.save(ship1);

            Ship ship2 = new Ship("destroyer",gamePlayer2,location2);
            shiprepository.save(ship2);

            Ship ship3 = new Ship("destroyer",gamePlayer2,location3);
            shiprepository.save(ship3);

            Ship ship4 = new Ship("patrol boat", gamePlayer3, location1);
            shiprepository.save(ship4);

            Ship ship5 = new Ship("cruiser",gamePlayer1, location1);
            shiprepository.save(ship5);

//
//            gamePlayer1.addShip(ship2);
//            gameplayerrepository.save(gamePlayer1);

//            gamePlayer2.addShip(ship2);
//            gamePlayer2.addShip(ship3);
//            gameplayerrepository.save(gamePlayer2);
//
//            gamePlayer3.addShip(ship1);
//            gamePlayer3.addShip(ship3);
//            gameplayerrepository.save(gamePlayer3);



        };
    }
}
