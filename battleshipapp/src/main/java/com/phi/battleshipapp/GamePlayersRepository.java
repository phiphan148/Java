package com.phi.battleshipapp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;

@Controller
public interface GamePlayersRepository extends JpaRepository<GamePlayer, Long> {

}
