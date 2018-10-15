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
    public CommandLineRunner initGamePlayer(PlayersRepository repository1, GameRepository repository2, GamePlayerRepository repository3, ShipRepository repository4){
        return (args) -> {
            Player player1 = new Player("Phi", "Phan", "phi@gmail.com");
            repository1.save(player1);
            Player player2 = new Player("Alain", "Phillipart de Foy", "alain@gmail.com");
            repository1.save(player2);
            Player player3 = new Player("Beatle", "Phan", "beatle@gmail.com");
            repository1.save(player3);
            Game game1 = new Game();
            repository2.save(game1);
            Game game2 = new Game();
            game2.setTodayDate(Date.from(new Date().toInstant().plusSeconds(3600)));
            repository2.save(game2);
            Game game3 = new Game();
            game3.setTodayDate(Date.from(new Date().toInstant().plusSeconds(7200)));
            repository2.save(game3);

            GamePlayer gamePlayer1 = new GamePlayer(game1,player2);
            repository3.save(gamePlayer1);
            GamePlayer gamePlayer2 = new GamePlayer(game1, player3);
            repository3.save(gamePlayer2);
            GamePlayer gamePlayer3 = new GamePlayer(game3, player2);
            repository3.save(gamePlayer3);

            List<String> locations = Arrays.asList("H3", "H2", "G1");
            List<String> locations2 = Arrays.asList("G1", "H1", "G3");
            List<String> locations3 = Arrays.asList("G5", "H6", "G4");

            Ship ship1 = new Ship("cruiser",gamePlayer1,locations);
            repository4.save(ship1);

            Ship ship2 = new Ship("cruiser",gamePlayer2,locations);
            repository4.save(ship2);


            Ship ship3 = new Ship("destroy",gamePlayer3,locations2);
            repository4.save(ship3);

            gamePlayer1.addShip(ship1);
            gamePlayer1.addShip(ship2);
            repository3.save(gamePlayer1);

//            gamePlayer2.addShip(ship2);
//            gamePlayer2.addShip(ship3);
//            repository3.save(gamePlayer2);
//
//            gamePlayer3.addShip(ship1);
//            gamePlayer3.addShip(ship3);
//            repository3.save(gamePlayer3);



        };
    }
}
