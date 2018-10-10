package com.phi.battleshipapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
public class BattleshipappApplication {

    public static void main(String[] args) {
        SpringApplication.run(BattleshipappApplication.class, args);
    }

    @Bean
    public CommandLineRunner initGamePlayer(PlayersRepository repository1, GameRepository repository2, GamePlayersRepository repository3){
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
        };
    }
}
