package com.phi.battleshipapp;//package com.phi.battleshipapp;

import com.phi.battleshipapp.persistence.model.*;
import com.phi.battleshipapp.persistence.repo.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class BattleshipappApplication {

    public static void main(String[] args) {
        SpringApplication.run(BattleshipappApplication.class, args);
    }

    @Bean
    public CommandLineRunner initGamePlayer(PlayersRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository, SalvoRepository salvoRepository, ScoreRepository scoreRepository){
        return (args) -> {
            Player player1 = new Player("Phi", "Phan", "phi@gmail.com");
            playerRepository.save(player1);
            Player player2 = new Player("Alain", "Phan", "alain@gmail.com");
            playerRepository.save(player2);
            Player player3 = new Player("Beatle", "Phan", "beatle@gmail.com");
            playerRepository.save(player3);

            Game game1 = new Game();
            gameRepository.save(game1);
            Game game2 = new Game();
            game2.setTodayDate(Date.from(new Date().toInstant().plusSeconds(3600)));
            gameRepository.save(game2);
            Game game3 = new Game();
            game3.setTodayDate(Date.from(new Date().toInstant().plusSeconds(7200)));
            gameRepository.save(game3);

            GamePlayer gamePlayer1 = new GamePlayer(game1,player1);
            gamePlayerRepository.save(gamePlayer1);
            GamePlayer gamePlayer2 = new GamePlayer(game3, player1);
            gamePlayerRepository.save(gamePlayer2);
            GamePlayer gamePlayer3 = new GamePlayer(game3, player2);
            gamePlayerRepository.save(gamePlayer3);
            GamePlayer gamePlayer4 = new GamePlayer(game2, player3);
            gamePlayerRepository.save(gamePlayer4);

            List<String> location1 = Arrays.asList("A3", "C2", "G1");
            List<String> location2 = Arrays.asList("G1", "F1", "G3");
            List<String> location3 = Arrays.asList("G5", "D6", "B4");

            Ship ship1 = new Ship("cruiser",gamePlayer1,location1);
            shipRepository.save(ship1);

            Ship ship2 = new Ship("destroyer",gamePlayer2,location2);
            shipRepository.save(ship2);

            Ship ship3 = new Ship("destroyer",gamePlayer4,location3);
            shipRepository.save(ship3);

            Ship ship4 = new Ship("patrol boat", gamePlayer3, location1);
            shipRepository.save(ship4);

            Ship ship5 = new Ship();
            ship5.setShipType("cruiser");
            ship5.setLocations(location3);
            gamePlayer1.addShip(ship5);
            shipRepository.save(ship5);
            gamePlayerRepository.save(gamePlayer1);

            List<String> turnLocation1 = Arrays.asList("A3","C6","G3");
            List<String> turnLocation2 = Arrays.asList("D5","C2","H3");
            List<String> turnLocation3 = Arrays.asList("G5","C2","G1");

            Salvo salvo1 = new Salvo();
            salvo1.setTurn(5);
            salvo1.setTurnLocation(turnLocation1);
            gamePlayer1.addSalvo(salvo1);
            salvoRepository.save(salvo1);
            gamePlayerRepository.save(gamePlayer1);

            Salvo salvo2 = new Salvo(3,turnLocation3);
            gamePlayer1.addSalvo(salvo2);
            salvoRepository.save(salvo2);
            gamePlayerRepository.save(gamePlayer1);

            Salvo salvo3 = new Salvo(2,turnLocation1);
            gamePlayer3.addSalvo(salvo3);
            salvoRepository.save(salvo3);
            gamePlayerRepository.save(gamePlayer3);

            Salvo salvo4 = new Salvo(8,turnLocation3);
            gamePlayer2.addSalvo(salvo4);
            salvoRepository.save(salvo4);
            gamePlayerRepository.save(gamePlayer2);

            Salvo salvo5 = new Salvo(6,turnLocation2);
            gamePlayer3.addSalvo(salvo5);
            salvoRepository.save(salvo5);
            gamePlayerRepository.save(gamePlayer3);

            Salvo salvo6 = new Salvo(10,turnLocation1);
            gamePlayer4.addSalvo(salvo6);
            salvoRepository.save(salvo6);
            gamePlayerRepository.save(gamePlayer4);

            Date finishDate1 = Date.from(new Date().toInstant().plusSeconds(86400));
//            Score score1 = new Score(0.5,game1,player1,finishDate1);
//            scoreRepository.save(score1);

//            Score score2 = new Score(0,game2,player3,finishDate1);
//            scoreRepository.save(score2);

            Score score3 = new Score(0.5,game3,player2,finishDate1);
            scoreRepository.save(score3);

            Score score4 = new Score(1,game3,player1,finishDate1);
            scoreRepository.save(score4);

        };
    }
}
